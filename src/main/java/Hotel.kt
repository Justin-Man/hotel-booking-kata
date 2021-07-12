import booking.Booking
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Hotel(override val id: Int) : WithId<Int> {

    var rooms = mutableListOf<Room>()
    val bookedRoomsDiary = hashMapOf<LocalDate, List<Room>>()

    fun setRoom(number: Int, roomType: RoomType) {
        val room = Room(number, roomType)
        rooms.find { room -> room.number == number }?.setRoomType(roomType)
            ?: rooms.add(room)
    }

    fun addReservation(booking: Booking.Success) {
        var date = booking.checkIn
        while (date.isBefore(booking.checkOut)) {

            if (bookedRoomsDiary[date] != null) {
                bookedRoomsDiary[date]?.plus(booking.room)
            } else {
                bookedRoomsDiary[date] = listOf(booking.room)
            }

            date = date.plus(1, ChronoUnit.DAYS)
        }
    }

    fun checkRoomAvailability(checkInDate: LocalDate, checkOutDate: LocalDate): List<Room> {
        var date = checkInDate
        val allRooms = rooms
        while (!date.isAfter(checkOutDate)) {
            if (bookedRoomsDiary[date] != null) {
                bookedRoomsDiary[date]?.forEach { bookedRoom ->
                    allRooms.removeIf { it.number == bookedRoom.number }
                }
            }

            date = date.plus(1, ChronoUnit.DAYS)
        }

        return allRooms
    }
}