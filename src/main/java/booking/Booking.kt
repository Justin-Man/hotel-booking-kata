package booking

import RoomType
import java.util.*

sealed class Booking {
    data class Success(
        val bookingId: String,
        val employeeId: String,
        val hotelId: Int,
        val roomType: RoomType,
        val checkIn: Date,
        val checkOut: Date
    )

    sealed class Error : Booking() {
        object AgainstEmployeePolicy : Error()
    }

}
