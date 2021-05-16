package company

import WithId

class Company(override val id: Int) : WithId<Int> {

    val employees = mutableListOf<Int>()
}