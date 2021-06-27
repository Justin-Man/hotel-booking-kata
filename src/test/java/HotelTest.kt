import org.junit.Test
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat

class HotelTest {

    private val hotel = Hotel(1)

    @Test
    fun `returns rooms that have not been booked at all`() {
        val checkIn = LocalDate.now()
        val checkOut = checkIn.plusDays(5)
        val room = Room(1, RoomType.StandardSingle)
        hotel.roomsWithNoBooking.add(room)

        val result = hotel.checkRoomAvailability(checkIn, checkOut)

        assertThat(result).isEqualTo(listOf(room))
    }

    @Test
    fun `returns rooms available for the whole booking period`() {
        val checkIn = LocalDate.now()
        val checkOut = checkIn.plusDays(5)
        val room1 = Room(1, RoomType.StandardSingle)
        val room2 = Room(2, RoomType.StandardSingle)
        val room3 = Room(3, RoomType.StandardDouble)
        hotel.rooms.addAll(listOf(room1, room2, room3))
        hotel.bookedRoomsDiary[checkIn.plusDays(2)] = listOf(room2)
        hotel.bookedRoomsDiary[checkOut.plusDays(1)] = listOf(room3)

        val result = hotel.checkRoomAvailability(checkIn, checkOut)

        assertThat(result).isEqualTo(listOf(room1, room3))
    }

    @Test
    fun `returns no rooms available for the whole booking period`() {
        val checkIn = LocalDate.now()
        val checkOut = checkIn.plusDays(5)
        val room1 = Room(1, RoomType.StandardSingle)
        val room2 = Room(2, RoomType.StandardSingle)
        val room3 = Room(3, RoomType.StandardDouble)
        hotel.rooms.addAll(listOf(room1, room2, room3))
        hotel.bookedRoomsDiary[checkIn.plusDays(3)] = listOf(room1)
        hotel.bookedRoomsDiary[checkIn.plusDays(2)] = listOf(room2)
        hotel.bookedRoomsDiary[checkIn.plusDays(1)] = listOf(room3)

        val result = hotel.checkRoomAvailability(checkIn, checkOut)

        assertThat(result).isEqualTo(emptyList<Room>())
    }
}