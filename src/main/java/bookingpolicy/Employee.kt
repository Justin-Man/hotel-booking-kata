package bookingpolicy

import WithId

data class Employee(override val id: Int, val companyId: Int) : WithId<Int>
