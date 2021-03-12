interface HotelDatabase {

    val hotels: MutableList<Hotel>
}

class InMemoryHotelDatabaseImpl : HotelDatabase {

    override val hotels = mutableListOf<Hotel>()
}