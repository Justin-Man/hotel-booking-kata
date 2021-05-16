class HotelService(private val hotelRepository: Repository<Int, Hotel>) {

    fun setRoom(hotelId: Int, number: Int, roomType: RoomType) {
        hotelRepository.find(hotelId)?.let { hotel ->
            hotel.setRoom(number, roomType)
            hotelRepository.update(hotel)
        } ?: throw HotelNotFoundException()
    }

    fun addHotel(hotel: Hotel) {
        hotelRepository.add(hotel)
    }

    fun findHotelBy(hotelId: Int): Hotel {
        return hotelRepository.find(hotelId) ?: throw HotelNotFoundException()
    }
}

enum class RoomType {
    standardSingle,
    standardDouble,
    juniorSuite,
    masterSuite
}

class HotelNotFoundException : Exception()
class HotelIDAlreadyExistsException : Exception()