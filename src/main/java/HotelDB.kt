interface HotelDB {

    fun find(hotelId: Int): Hotel?

    fun update(hotel: Hotel)
}

class HotelDBImpl : HotelDB {

    private val hotels: List<Hotel> = mutableListOf()

    override fun find(hotelId: Int): Hotel? {
        return hotels.find { it.hotelId == hotelId }
    }

    override fun update(hotel: Hotel) {
        hotels.find { it.hotelId == hotel.hotelId }.
    }

}