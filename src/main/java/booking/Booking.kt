package booking

import Room
import java.time.LocalDate

sealed class Booking {
    sealed class Error : Booking() {
        object AgainstEmployeePolicy : Error()
        object InvalidCheckOutDate : Error()
        object InvalidHotel : Error()
        object InvalidRoomType : Error()
        object NoRoomsAvailable : Error()
    }

    data class Success(
        val bookingId: Int,
        val employeeId: Int,
        val hotelId: Int,
        val checkIn: LocalDate,
        val checkOut: LocalDate,
        val room: Room
    ) : Booking()
}
