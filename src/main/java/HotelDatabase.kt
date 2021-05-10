interface HotelDatabase {

    val hotels: MutableList<Hotel>
}

class InMemoryHotelDatabaseImpl : Database<Int, Hotel>(), HotelDatabase {

    override val hotels = mutableListOf<Hotel>()
}

open class Database<K, V>(private val table : MutableMap<K, V> = mutableMapOf()) {

}