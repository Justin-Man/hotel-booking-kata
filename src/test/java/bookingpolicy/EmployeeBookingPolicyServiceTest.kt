package bookingpolicy

import MemoryDatabase
import Repository
import RepositoryImpl
import RoomType
import company.Company
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class EmployeeBookingPolicyServiceTest {

    lateinit var employeeBookingPolicyDatabase: MemoryDatabase<Int, EmployeeBookingPolicy>
    lateinit var employeeBookingPolicyRepository: RepositoryImpl<Int, EmployeeBookingPolicy>
    lateinit var companyBookingPolicyDatabase: MemoryDatabase<Int, CompanyBookingPolicy>
    lateinit var companyBookingPolicyRepository: Repository<Int, CompanyBookingPolicy>
    private lateinit var bookingPolicyService : BookingPolicyService
    lateinit var companyDatabase: MemoryDatabase<Int, Company>
    lateinit var companyRepository : Repository<Int, Company>
    lateinit var employeeDatabase: MemoryDatabase<Int, Employee>
    lateinit var employeeRepository: Repository<Int, Employee>

    @Before
    fun setUp() {
        companyDatabase = MemoryDatabase()
        companyRepository = RepositoryImpl(companyDatabase)

        employeeBookingPolicyDatabase = MemoryDatabase()
        companyBookingPolicyDatabase = MemoryDatabase()

        companyBookingPolicyRepository = RepositoryImpl(companyBookingPolicyDatabase)
        employeeBookingPolicyRepository = RepositoryImpl(employeeBookingPolicyDatabase)

        employeeDatabase = MemoryDatabase()
        employeeRepository = RepositoryImpl(employeeDatabase)

        bookingPolicyService = BookingPolicyService(
            employeeBookingPolicyRepository,
            companyBookingPolicyRepository,
            companyRepository
        )
    }

    @Test
    fun `sets employee booking policy for employee`() {
        val employeeId = 1
        val allowedRoomtypes = listOf(RoomType.StandardSingle, RoomType.JuniorSuite)

        bookingPolicyService.setEmployeePolicy(employeeId, allowedRoomtypes)

        assertThat(employeeBookingPolicyDatabase.table[employeeId]!!.allowedRoomTypes)
            .isEqualTo(listOf(RoomType.StandardSingle, RoomType.JuniorSuite))
    }

    @Test
    fun `disallows room to be booked when room type is not in employee booking policy`() {
        val roomType = RoomType.MasterSuite
        val employeeId = 1
        val companyId = 100
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company
        employeeBookingPolicyDatabase.table[employeeId] = EmployeeBookingPolicy(employeeId, listOf(RoomType.JuniorSuite))

        val result = bookingPolicyService.isBookingAllowed(employeeId, roomType)

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `allows room booking when no employee or company policy exists`() {
        val roomType = RoomType.MasterSuite
        val employeeId = 1
        val companyId = 100
        employeeBookingPolicyDatabase.table[employeeId] = EmployeeBookingPolicy(employeeId, emptyList())
        companyBookingPolicyDatabase.table[companyId] = CompanyBookingPolicy(companyId, emptyList())
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company

        val result = bookingPolicyService.isBookingAllowed(employeeId, roomType)

        assertThat(result).isEqualTo(true)
    }
}