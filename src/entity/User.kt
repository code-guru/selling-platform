package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(Users)

    var firstName by Users.firstname
    var lastName by Users.lastname
    var email by Users.email
    val ads by AdDao referrersOn Ads.ownerId
    fun toModel(): User {
        return User(id.value, firstName, lastName, email)
    }
}

data class User(
    override var id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
) : DataEntity()

object Users : IntIdTable() {
    val email: Column<String> = varchar("email", 50).uniqueIndex()
    val firstname: Column<String> = varchar("firstname", 50)
    val lastname: Column<String> = varchar("lastname", 50)
}
