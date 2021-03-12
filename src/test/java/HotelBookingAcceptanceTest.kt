import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import kotlin.test.assertFailsWith

class HotelBookingAcceptanceTest {

    lateinit var hotelDB: HotelDB
    private val hotelRepository = HotelRepository(hotelDB)
    private val hotelService = HotelService(hotelRepository)

    @Before
    fun setUp() {
        hotelDB = InMemoryHotelDBImpl()
    }

    @Test
    fun `GIVEN hotel exists WHEN user sets room THEN room updated`() {
        given(hotelDB.find(hotelId)).willReturn(hotel)

        hotelService.setRoom(hotelId, number, roomType)

        verify(hotelDB).update(hotel)
    }
}

val hotelId = 1
val hotel = Hotel(hotelId)
val number = 11
val roomType = RoomType.standardSingle