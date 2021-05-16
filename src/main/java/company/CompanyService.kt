package company

import Repository

class CompanyService(private val companyRepository: Repository<Int, Company>) {
    fun addEmployee(companyId: Int, vararg employeeId: Int) {
        val company = companyRepository.find(companyId)
        employeeId.forEach {
            if (company?.employees?.contains(it) == false) {
                company.employees.add(it)
            }
        }
    }
}