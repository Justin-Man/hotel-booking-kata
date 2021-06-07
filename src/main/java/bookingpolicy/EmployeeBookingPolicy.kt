package bookingpolicy

import RoomType
import WithId

data class EmployeeBookingPolicy(override val id: Int, var allowedRoomTypes: List<RoomType>) : WithId<Int>
