import company.Company

interface Repository<K, V : WithId<K>> {
    fun add(value: V)

    fun update(value: V)

    fun find(id: K): V?

    fun findKey(id: K): Int
}

class RepositoryImpl<K, V : WithId<K>>(private val database: Database<K, V>) : Repository<K, V> {
    override fun add(value: V) {
        if (find(value.id) != null) {
            throw AlreadyExistsException()
        } else {
            database.table[value.id] = value
        }
    }

    override fun update(value: V) {
        if (find(value.id) != null)
            database.table.replace(value.id, value)
        else
            throw NotFoundException()
    }

    override fun find(id: K): V? {
        return database.table[id]
    }

    override fun findKey(id: K): Int {
        val company = database.table.values.map { it as Company }.find { it.employees.contains(id) }

        return company!!.id
    }
}

interface WithId<K> {

    val id: K
}