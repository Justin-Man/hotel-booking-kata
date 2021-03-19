package company

class CompanyService(private val companyRepository: CompanyRepository) {
    fun addEmployee(companyId: Int, vararg employeeId: Int) {
        companyRepository.addEmployee(companyId = companyId, *employeeId)
    }
}