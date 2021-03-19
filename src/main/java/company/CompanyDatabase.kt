package company
interface CompanyDatabase {
    val employees: MutableMap<Int, List<Employee>>
}

class CompanyInMemoryDatabaseImpl : CompanyDatabase {

    override val employees = mutableMapOf<Int, List<Employee>>()
}