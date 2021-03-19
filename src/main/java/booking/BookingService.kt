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
//        if (bookingPolicyService.isBookingAllowed(employeeId, roomType)) {
//            return Booking.Error.AgainstEmployeePolicy
//        }
//        return Booking.Success(employeeId, hotelId, roomType,checkIn, checkOut)
        TODO()
    }
}
