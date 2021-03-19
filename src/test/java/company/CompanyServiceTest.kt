package company

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CompanyServiceTest {

    lateinit var companyDatabase: CompanyInMemoryDatabaseImpl
    lateinit var companyRepository : CompanyRepository
    private lateinit var companyService : CompanyService

    @Before
    fun setUp() {
        companyDatabase = CompanyInMemoryDatabaseImpl()
        companyRepository = CompanyRepository(companyDatabase)
        companyService = CompanyService(companyRepository)
    }

    @Test
    fun `adds employee that doesn't already exist`() {
        val companyId = 1
        val employeeId = 1

        companyService.addEmployee(companyId, employeeId)

        val employees = companyDatabase.employees[companyId]
        assertThat(employees!!.first().id).isEqualTo(employeeId)
    }

    @Test
    fun `does not add a duplicate employee id`() {
        val companyId = 1
        val employeeId = 1
        companyDatabase.employees[companyId] = listOf(Employee(employeeId))

        companyService.addEmployee(companyId = companyId, employeeId)

        assertThat(companyDatabase.employees[companyId]).isEqualTo(listOf(Employee(employeeId)))
    }

    @Test
    fun `does not add a duplicate employee id when the company does not have employees`() {
        val companyId = 1
        val employeeId = 1

        companyService.addEmployee(companyId = companyId, employeeId, employeeId)

        assertThat(companyDatabase.employees[companyId]).isEqualTo(listOf(Employee(employeeId)))
    }

    @Test
    fun `adds two distinct employee ids to the same company`() {
        val companyId = 1
        val employeeId = 1
        val employeeId2 = 2

        companyService.addEmployee(companyId= companyId, employeeId, employeeId2)

        val employees = companyDatabase.employees[companyId]
        assertThat(employees).isEqualTo(listOf(Employee(employeeId), Employee(employeeId2)))
    }
}