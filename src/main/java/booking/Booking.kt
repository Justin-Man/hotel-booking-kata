package booking

import java.util.*

sealed class Booking {
    sealed class Error : Booking() {
        object AgainstEmployeePolicy : Error()
        object InvalidCheckOutDate : Error()
        object InvalidHotel : Error()
        object InvalidRoomType : Error()
    }

    data class Success(
        val bookingId: Int,
        val employeeId: Int,
        val hotelId: Int,
        val checkIn: Date,
        val checkOut: Date
    ) : Booking()
}
