class HotelRepository(private val database: HotelDatabase) {

    fun find(hotelId: Int): Hotel? {
        return database.hotels.find { it.hotelId == hotelId }
    }

    fun update(hotel: Hotel) {
        val existingHotelIndex = database.hotels.indexOfFirst { it.hotelId == hotel.hotelId }

        if (existingHotelIndex != -1)
            database.hotels[existingHotelIndex] = hotel
        else
            database.hotels.add(hotel)
    }

    fun add(hotel: Hotel) {
        if (database.hotels.find { it.hotelId == hotel.hotelId } == null)
            database.hotels.add(hotel)
        else
            throw HotelIDAlreadyExistsException()

    }
}