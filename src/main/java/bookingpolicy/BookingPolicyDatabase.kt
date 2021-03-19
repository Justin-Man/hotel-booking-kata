package bookingpolicy

import RoomType

interface BookingPolicyDatabase {

    val companyBookingPolicies: MutableMap<Int, List<RoomType>>

    val employeesWithBookingPolicies: MutableMap<Int, List<RoomType>>
}

class BookingPolicyInMemoryDatabaseImpl : BookingPolicyDatabase{

    override val companyBookingPolicies = mutableMapOf<Int, List<RoomType>>()

    override val employeesWithBookingPolicies = mutableMapOf<Int, List<RoomType>>()
}
