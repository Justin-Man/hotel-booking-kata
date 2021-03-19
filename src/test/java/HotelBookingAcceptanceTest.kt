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
        assertThat(hotel.rooms.find { it.number == number && it.getRoomType() == roomType }).isNotNull
    }

    @Test
    fun `GIVEN hotel does not exist WHEN user sets room THEN exception thrown`() {
        assertFailsWith(HotelNotFoundException::class) {
            hotelService.setRoom(hotelId, number, roomType)
        }
    }

    @Test
    fun `GIVEN new hotel WHEN user adds hotel THEN hotel added to database`() {
        val hotel = Hotel(hotelId)

        hotelService.addHotel(hotel)

        val hotelInDatabase = hotelDatabase.hotels.find { it.hotelId == hotelId }
        assertThat(hotelInDatabase).isNotNull
    }

    @Test
    fun `GIVEN hotel already exists WHEN user adds hotel THEN exception thrown`() {
        val hotel = Hotel(hotelId)
        hotelDatabase.hotels.add(hotel)

        assertFailsWith(HotelIDAlreadyExistsException::class) {
            hotelService.addHotel(hotel)
        }
    }

    @Test
    fun `GIVEN hotel in database WHEN user queries hotel THEN room count returned`() {
        val hotel = Hotel(hotelId)
        hotel.rooms.add(Room(number, roomType))
        hotelDatabase.hotels.add(hotel)

        val result = hotelService.findHotelBy(hotelId)

        assertThat(result).isEqualTo(hotel)
        assertThat(result.rooms.count()).isEqualTo(1)
    }

    @Test
    fun `GIVEN hotel not in database WHEN user queries hotel THEN zero room count returned`() {
        assertFailsWith(HotelNotFoundException::class) {
            hotelService.findHotelBy(hotelId)
        }
    }
}

val hotelId = 1
val hotel = Hotel(hotelId)
val number = 11
val roomType = RoomType.standardSingle