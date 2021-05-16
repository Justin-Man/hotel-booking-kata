package company

import MemoryDatabase
import Repository
import RepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CompanyServiceTest {

    lateinit var companyDatabase: MemoryDatabase<Int, Company>
    lateinit var companyRepository : Repository<Int, Company>
    private lateinit var companyService : CompanyService

    @Before
    fun setUp() {
        companyDatabase = MemoryDatabase()
        companyRepository = RepositoryImpl(companyDatabase)
        companyService = CompanyService(companyRepository)
    }

    @Test
    fun `adds employee that doesn't already exist`() {
        val companyId = 1
        val employeeId = 1
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company

        companyService.addEmployee(companyId, employeeId)

        assertThat(companyDatabase.table[companyId]!!.employees.first()).isEqualTo(employeeId)
    }

    @Test
    fun `does not add a duplicate employee id`() {
        val companyId = 1
        val employeeId = 1
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company

        companyService.addEmployee(companyId, employeeId)

        assertThat(companyDatabase.table[companyId]?.employees).isEqualTo(listOf(employeeId))
    }

    @Test
    fun `adds only distinct employee id when duplicates supplied`() {
        val companyId = 1
        val employeeId = 1
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company

        companyService.addEmployee(companyId = companyId, employeeId, employeeId)

        assertThat(companyDatabase.table[companyId]!!.employees).isEqualTo(listOf(employeeId))
    }

    @Test
    fun `adds two distinct employee ids to the same company`() {
        val companyId = 1
        val employeeId = 1
        val employeeId2 = 2
        val company = Company(companyId)
        company.employees.add(employeeId)
        companyDatabase.table[companyId] = company

        companyService.addEmployee(companyId= companyId, employeeId, employeeId2)

        val employees = companyDatabase.table[companyId]?.employees
        assertThat(employees).isEqualTo(listOf(employeeId, employeeId2))
    }
}