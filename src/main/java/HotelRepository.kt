class HotelRepository(private val hotelDB: HotelDB) {

    fun find(hotelId: Int): Hotel? {
        return hotelDB.find(hotelId)
    }

    fun update(hotel: Hotel) {
        hotelDB.update(hotel)
    }
}