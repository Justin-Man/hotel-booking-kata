class HotelService(private val hotelRepository: HotelRepository) {

    fun setRoom(hotelId: Int, number: Int, roomType: RoomType) {
        hotelRepository.find(hotelId)?.let { hotel ->
            hotel.setRoom(number, roomType)
            hotelRepository.update(hotel)
        } ?: throw HotelNotFoundException()
    }

    fun addHotel(hotel: Hotel) {
        hotelRepository.add(hotel)
    }

    fun findHotelBy(hotelId: Int) =
        hotelRepository.find(hotelId)?.rooms?.count() ?: 0
}

enum class RoomType {
    standardSingle,
    standardDouble,
    juniorSuite,
    masterSuite
}

class HotelNotFoundException : Exception()
class HotelIDAlreadyExistsException : Exception()