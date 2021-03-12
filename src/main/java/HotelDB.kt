interface HotelDB {

    fun find(hotelId: Int): Hotel?

    fun update(hotel: Hotel)
}

class InMemoryHotelDBImpl : HotelDB {

    private val hotels = mutableListOf<Hotel>()

    override fun find(hotelId: Int): Hotel? {
        return hotels.find { it.hotelId == hotelId }
    }

    override fun update(hotel: Hotel) {
        val existingHotelIndex = hotels.indexOfFirst { it.hotelId == hotel.hotelId }

            if (existingHotelIndex != -1)
                hotels[existingHotelIndex] = hotel
            else
                hotels.add(hotel)
    }
}