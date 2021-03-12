import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class HotelBookingAcceptanceTest {

    lateinit var hotelDatabase: HotelDatabase
    private lateinit var hotelRepository: HotelRepository
    private lateinit var hotelService: HotelService

    @Before
    fun setUp() {
        hotelDatabase = InMemoryHotelDatabaseImpl()
        hotelRepository = HotelRepository(hotelDatabase)
        hotelService = HotelService(hotelRepository)
    }

    @Test
    fun `GIVEN hotel exists WHEN user sets room THEN room updated`() {
        hotelDatabase.hotels.add(Hotel(hotelId))

        hotelService.setRoom(hotelId, number, roomType)

        val hotel = hotelDatabase.hotels.first()

        assertThat(hotel.rooms.find { it.number == number && it.getRoomType() == roomType}).isNotNull
    }

    @Test
    fun `GIVEN hotel does not exist WHEN user sets room THEN exception thrown`() {
        assertFailsWith(HotelNotFoundException::class) {
            hotelService.setRoom(hotelId, number, roomType)
        }
    }
}

val hotelId = 1
val hotel = Hotel(hotelId)
val number = 11
val roomType = RoomType.standardSingle