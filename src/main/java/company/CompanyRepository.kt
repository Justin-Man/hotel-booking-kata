package company

class CompanyRepository(private val companyDatabase: CompanyDatabase) {

    fun addEmployee(companyId: Int, vararg employeeId: Int) {
         companyDatabase.employees[companyId]?.let {
             val employeeIds = it.map { employee -> employee.id }
             employeeId.toSet().forEach { employeeId ->
                 if(!(employeeIds.contains(employeeId))) {
                     companyDatabase.employees[companyId]?.plus(Employee(employeeId))
                 }
             }
        }
        companyDatabase.employees[companyId] = employeeId.toSet().map { Employee(it) }
    }

    fun findCompany(employeeId: Int): Int {
        return companyDatabase.employees.filter { it.value.contains(Employee(employeeId)) }.keys.first()
    }
}