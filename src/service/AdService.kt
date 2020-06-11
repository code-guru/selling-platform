package service

import entity.Ad
import entity.Ads
import entity.User
import entity.Users
import io.ktor.util.StringValues
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import services.ApiService
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AdService : ApiService<Ad> {
    private fun toAdModel(it: ResultRow): Ad {
        return Ad(
            it[Ads.id].value,
            it[Ads.title],
            it[Ads.description],
            it[Ads.category],
            it[Ads.creationDate],
            it[Ads.ownerId],
            User(
                it[Users.id].value,
                it[Users.firstname],
                it[Users.lastname],
                it[Users.email]
            )
        )
    }

    override fun getAll(filterParams: StringValues?): List<Ad> {
        val category = filterParams!!["category"]
        val fromDate = filterParams["from"]
        val toDate = filterParams["to"]
        fun parseDate(str: String) = LocalDate.parse(str, DateTimeFormatter.ISO_DATE)

        return transaction {
            val query = Ads.innerJoin(Users).selectAll()

            if (category != null) query.andWhere { Ads.category eq category }
            if (fromDate != null) query.andWhere { Ads.creationDate greaterEq parseDate(fromDate) }
            if (toDate != null) query.andWhere { Ads.creationDate lessEq parseDate(toDate) }

            query.map { toAdModel(it) }
        }
    }

    override fun findOne(id: Int): Ad? {
        return transaction {
            Ads.innerJoin(Users).select({ Ads.id eq id }).map { toAdModel(it) }.first()
        }
    }

    override fun insert(entity: Ad): Int {
        return transaction {
            Ads.insertAndGetId {
                it[title] = entity.title
                it[category] = entity.category
                it[description] = entity.description ?: ""
                it[ownerId] = entity.ownerId
                it[creationDate] = entity.creationDate!!
            }

        }.value
    }

    override fun update(entity: Ad, id: Int): Int {
        return transaction {
            Ads.update({ Ads.id eq id }) {
                it[title] = entity.title
                it[category] = entity.category
                it[description] = entity.description ?: ""
                it[ownerId] = entity.ownerId
                it[creationDate] = entity.creationDate!!
            }
        }
    }

    override fun delete(id: Int): Int {
        return transaction {
            Ads.deleteWhere { Ads.id eq id }
        }
    }
}
