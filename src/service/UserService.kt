package service

import entity.User
import entity.UserDao
import entity.Users
import io.ktor.util.StringValues
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import services.ApiService

class UserService : ApiService<User> {
    override fun getAll(filterParams: StringValues?): List<User> {
        return transaction {
            UserDao.all().toList().map { it.toModel() }
        }
    }

    override fun findOne(id: Int): User? {
        return transaction {
            UserDao.findById(id)?.toModel()
        }
    }


    override fun insert(entity: User): Int {
        return transaction {
            UserDao.new {
                email = entity.email
                firstName = entity.firstName
                lastName = entity.lastName
            }

        }.id.value
    }

    override fun update(entity: User, id: Int): Int {
        return transaction {
            Users.update({ Users.id eq id }) {
                it[email] = entity.email
                it[firstname] = entity.firstName
                it[lastname] = entity.lastName
            }
        }
    }

    override fun delete(id: Int): Int {
        return transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}
