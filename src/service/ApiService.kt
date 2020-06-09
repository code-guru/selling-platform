package services

import io.ktor.util.StringValues

interface ApiService<T> {
    fun getAll(filterParams: StringValues? = StringValues.Empty): List<T>
    fun findOne(id: Int): T?
    fun insert(entity: T): Int
    fun update(entity: T, id: Int): Int
    fun delete(id: Int): Int
}
