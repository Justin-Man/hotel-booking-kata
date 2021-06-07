import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class HotelServiceTest {

    lateinit var database: MemoryDatabase<Int, Hotel>
    private lateinit var repository: RepositoryImpl<Int, Hotel>
    private lateinit var hotelService: HotelService

    @Before
    fun setUp() {
        database = MemoryDatabase()
        repository = RepositoryImpl(database)
        hotelService = HotelService(repository)
    }

    @Test
    fun `GIVEN hotel exists WHEN user sets room THEN room updated`() {
        database.table[hotelId] = Hotel(hotelId)

        hotelService.setRoom(hotelId, number, roomType)

        val hotel = database.table[hotelId]
        assertThat(hotel!!.rooms.find { it.number == number && it.getRoomType() == roomType}).isNotNull
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

        val hotelInDatabase = database.table[hotelId]
        assertThat(hotelInDatabase).isNotNull
    }

    @Test
    fun `GIVEN hotel already exists WHEN user adds hotel THEN exception thrown`() {
        val hotel = Hotel(hotelId)
        database.table[hotelId] = hotel

        assertFailsWith(AlreadyExistsException::class) {
            hotelService.addHotel(hotel)
        }
    }

    @Test
    fun `GIVEN hotel in database WHEN user queries hotel THEN hotel returned`() {
        val hotel = Hotel(hotelId)
        hotel.rooms.add(Room(number, roomType))
        database.table[hotelId] = hotel

        val roomCount = hotelService.findHotelBy(hotelId)

        assertThat(roomCount).isEqualTo(hotel)
    }

    @Test
    fun `GIVEN hotel not in database WHEN user queries hotel THEN exception thrown`() {
        assertFailsWith(HotelNotFoundException::class) {
            hotelService.findHotelBy(hotelId)
        }
    }
}

val hotelId = 1
val hotel = Hotel(hotelId)
val number = 11
val roomType = RoomType.StandardSingle