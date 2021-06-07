package bookingpolicy

import RoomType
import WithId

data class CompanyBookingPolicy(
    override val id: Int,
    val allowedRoomTypes: List<RoomType>
) : WithId<Int>
