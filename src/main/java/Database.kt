interface Database<K, V> {

    val table: MutableMap<K, V>
}

// For testing
open class MemoryDatabase<K, V>(override val table : MutableMap<K, V> = mutableMapOf()) : Database<K, V>