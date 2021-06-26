import booking.Booking
import java.time.Instant
import java.time.temporal.ChronoUnit

class Hotel(override val id: Int) : WithId<Int> {

    var rooms = mutableListOf<Room>()
    val bookedRoomsDiary = hashMapOf<Instant, List<Booking>>()

    fun setRoom(number: Int, roomType: RoomType) {
        val room = Room(number, roomType)
        rooms.find { room -> room.number == number }?.setRoomType(roomType)
            ?: rooms.add(room)
    }

    fun addReservation(booking: Booking.Success) {
        var date = booking.checkIn.toInstant()
        while (date.isBefore(booking.checkOut.toInstant())) {

            if (bookedRoomsDiary[date] != null) {
                bookedRoomsDiary[date]?.plus(booking)
            } else {
                bookedRoomsDiary[date] = listOf(booking)
            }

            date = date.plus(1, ChronoUnit.DAYS)
        }
    }
}