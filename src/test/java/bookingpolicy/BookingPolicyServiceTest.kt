package bookingpolicy

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import company.CompanyInMemoryDatabaseImpl
import company.CompanyRepository
import company.Employee
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class BookingPolicyServiceTest {

    lateinit var bookingPolicyDatabase: BookingPolicyInMemoryDatabaseImpl
    lateinit var bookingPolicyRepository : BookingPolicyRepository
    private lateinit var bookingPolicyService : BookingPolicyService
    lateinit var companyDatabase: CompanyInMemoryDatabaseImpl
    lateinit var companyRepository : CompanyRepository

    @Before
    fun setUp() {
        companyDatabase = CompanyInMemoryDatabaseImpl()
        companyRepository = CompanyRepository(companyDatabase)
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
        companyDatabase.employees[companyId] = listOf(Employee(employeeId))
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
        companyDatabase.employees[companyId] = listOf(Employee(employeeId))

        val result = bookingPolicyService.isBookingAllowed(employeeId, roomType)

        assertThat(result).isEqualTo(true)
    }
}