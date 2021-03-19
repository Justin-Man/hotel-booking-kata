package booking

import RoomType
import bookingpolicy.BookingPolicyService
import java.util.*

class BookingService(
    private val bookingPolicyService: BookingPolicyService
) {

    fun book(
        employeeId: Int,
        hotelId: Int,
        roomType: RoomType,
        checkIn: Date,
        checkOut: Date
    ): Booking {
        return when {
            !bookingPolicyService.isBookingAllowed(employeeId, roomType) ->
                Booking.Error.AgainstEmployeePolicy

            else -> TODO()
        }

    }
}
