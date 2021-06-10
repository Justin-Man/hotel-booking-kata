package booking

import Hotel
import HotelNotFoundException
import HotelService
import RoomType
import bookingpolicy.BookingPolicyService
import java.util.*

class BookingService(
    private val bookingPolicyService: BookingPolicyService,
    private val hotelService: HotelService
) {

    fun book(
        employeeId: Int,
        hotelId: Int,
        roomType: RoomType,
        checkIn: Date,
        checkOut: Date
    ): Booking {

        try {
            val hotel = hotelService.findHotelBy(hotelId)
            if (!(hotel.rooms.map { it.getRoomType() }.contains(roomType)))
                return Booking.Error.InvalidRoomType
        } catch (e: HotelNotFoundException) {
            return Booking.Error.InvalidHotel
        }

        return when {
            !bookingPolicyService.isBookingAllowed(employeeId, roomType) -> Booking.Error.AgainstEmployeePolicy
            !checkOut.after(checkIn) -> Booking.Error.InvalidCheckOutDate

            else -> TODO()
        }

    }
}
