package bookingpolicy

import MemoryDatabase
import Repository
import RepositoryImpl
import RoomType
import company.Company
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CompanyBookingPolicyServiceTest {

    lateinit var companyBookingPolicyDatabase: MemoryDatabase<Int, CompanyBookingPolicy>
    lateinit var companyBookingPolicyRepository: Repository<Int, CompanyBookingPolicy>
    private lateinit var bookingPolicyService : BookingPolicyService
    lateinit var companyDatabase: MemoryDatabase<Int, Company>
    lateinit var companyRepository : Repository<Int, Company>
    lateinit var employeeBookingPolicyDatabase: MemoryDatabase<Int, EmployeeBookingPolicy>
    lateinit var employeeBookingPolicyRepository: Repository<Int, EmployeeBookingPolicy>
    lateinit var employeeDatabase: MemoryDatabase<Int, Employee>
    lateinit var employeeRepository: Repository<Int, Employee>

    @Before
    fun setUp() {
        companyDatabase = MemoryDatabase()
        companyRepository = RepositoryImpl(companyDatabase)

        employeeBookingPolicyDatabase = MemoryDatabase()
        employeeBookingPolicyRepository = RepositoryImpl(employeeBookingPolicyDatabase)

        companyBookingPolicyDatabase = MemoryDatabase()
        companyBookingPolicyRepository = RepositoryImpl(companyBookingPolicyDatabase)

        employeeDatabase = MemoryDatabase()
        employeeRepository = RepositoryImpl(employeeDatabase)

        bookingPolicyService = BookingPolicyService(
            employeeBookingPolicyRepository,
            companyBookingPolicyRepository,
            companyRepository,
            employeeRepository
        )
    }

    @Test
    fun `sets company booking policy`() {
        val companyId = 1
        val allowedRoomtypes = listOf(RoomType.StandardSingle, RoomType.JuniorSuite)

        bookingPolicyService.setCompanyPolicy(companyId, allowedRoomtypes)

        assertThat(companyBookingPolicyDatabase.table[companyId]!!.allowedRoomTypes).isEqualTo(allowedRoomtypes)
    }
}