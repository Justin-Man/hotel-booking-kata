package bookingpolicy

import MemoryDatabase
import Repository
import RepositoryImpl
import company.Company
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class BookingPolicyServiceTest {

    lateinit var bookingPolicyDatabase: BookingPolicyInMemoryDatabaseImpl
    lateinit var bookingPolicyRepository : BookingPolicyRepository
    private lateinit var bookingPolicyService : BookingPolicyService
    lateinit var companyDatabase: MemoryDatabase<Int, Company>
    lateinit var companyRepository : Repository<Int, Company>

    @Before
    fun setUp() {
        companyDatabase = MemoryDatabase()
        companyRepository = RepositoryImpl(companyDatabase)
        bookingPolicyDatabase = BookingPolicyInMemoryDatabaseImpl()
        bookingPolicyRepository = BookingPolicyRepository(bookingPolicyDatabase)
        bookingPolicyService = BookingPolicyService(bookingPolicyRepository, companyRepository)
    }

    @Test
    fun `sets employee booking policy for employee`() {
        val employeeId = 1
        val allowedRoomtypes = listOf(RoomType.standardSingle, RoomType.juniorSuite)

        bookingPolicyService.setEmployeePolicy(employeeId, allowedRoomtypes)

        assertThat(bookingPolicyDatabase.employeesWithBookingPolicies[employeeId])
            .isEqualTo(listOf(RoomType.standardSingle, RoomType.juniorSuite))
    }

    @Test
    fun `disallows room to be booked when room type is not in employee booking policy`() {
        val roomType = RoomType.masterSuite
        val employeeId = 1
        val companyId = 100
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company
        bookingPolicyDatabase.employeesWithBookingPolicies[employeeId] = listOf(RoomType.juniorSuite)

        val result = bookingPolicyService.isBookingAllowed(employeeId, roomType)

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `allows room booking when no employee or company policy exists`() {
        val roomType = RoomType.masterSuite
        val employeeId = 1
        val companyId = 100
        bookingPolicyDatabase.employeesWithBookingPolicies[employeeId] = emptyList()
        bookingPolicyDatabase.companyBookingPolicies[companyId] = emptyList()
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company

        val result = bookingPolicyService.isBookingAllowed(employeeId, roomType)

        assertThat(result).isEqualTo(true)
    }
}