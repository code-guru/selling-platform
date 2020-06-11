package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

class AdDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AdDao>(Ads)

    var title by Ads.title
    var description by Ads.description
    var category by Ads.category
    var creationDate by Ads.creationDate
    val owner by UserDao referrersOn Ads.ownerId

    fun toModel(): Ad {
        val ownerUser = owner.first().toModel()
        return Ad(id.value, title, description, category, creationDate, ownerUser.id, ownerUser)
    }
}

data class Ad(
    override var id: Int,
    val title: String,
    val description: String?,
    val category: String,
    var creationDate: LocalDate?,
    val ownerId: Int,
    val owner: User?
) : DataEntity()

object Ads : IntIdTable() {
    val title = text("title")
    val description = text("description")
    val category = text("category")
    val creationDate = date("creationDate")
    val ownerId = integer("ownerId").references(Users.id)
}
