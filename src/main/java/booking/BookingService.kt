package booking

import HotelNotFoundException
import HotelService
import RoomType
import bookingpolicy.BookingPolicyService
import java.time.LocalDate

class BookingService(
    private val bookingPolicyService: BookingPolicyService,
    private val hotelService: HotelService
) {

    fun book(
        employeeId: Int,
        hotelId: Int,
        roomType: RoomType,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): Booking {

        try {
            val hotel = hotelService.findHotelBy(hotelId)
            if (!(hotel.rooms.map { it.getRoomType() }.contains(roomType)))
                return Booking.Error.InvalidRoomType
            if (hotel.checkRoomAvailability(checkIn, checkOut).isEmpty())
                return Booking.Error.NoRoomsAvailable
        } catch (e: HotelNotFoundException) {
            return Booking.Error.InvalidHotel
        }

        return when {
            !bookingPolicyService.isBookingAllowed(employeeId, roomType) -> Booking.Error.AgainstEmployeePolicy
            !checkOut.isAfter(checkIn) -> Booking.Error.InvalidCheckOutDate

            else -> {
                val hotel = hotelService.findHotelBy(hotelId)
                val room = hotel.checkRoomAvailability(checkIn, checkOut).first { it.getRoomType() == roomType }
                val booking = Booking.Success("$employeeId$hotelId".toInt(), employeeId, hotelId, checkIn, checkOut, room)
                hotel.addReservation(booking)
                booking
            }
        }

    }
}
