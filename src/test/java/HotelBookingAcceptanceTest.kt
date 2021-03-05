import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.mockito.BDDMockito.given

class HotelBookingAcceptanceTest {

    val hotelDB: HotelDB = mock()
    val hotelRepository = HotelRepository(hotelDB)
    val hotelService = HotelService(hotelRepository)

    @Test
    fun `GIVEN hotel exists WHEN user sets room THEN room updated`() {
        val hotelId = 1
        val hotel = Hotel(hotelId)
        val number = 11
        val roomType = RoomType.standardSingle
        given(hotelDB.find(hotelId)).willReturn(hotel)

        hotelService.setRoom(hotelId, number, roomType)

        verify(hotelDB).update(hotel)
    }

    // GIVEN hotel does not exist WHEN user sets room THEN exception thrown
}