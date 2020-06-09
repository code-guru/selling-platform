package helpers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import entity.Ads
import entity.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB(testing: Boolean) {
    val configFileName = if (testing) "/hikari.test.properties" else "/hikari.properties"
    val config = HikariConfig(configFileName)
    if(!testing) setDBConfigFromEnv(config)

    val ds = HikariDataSource(config)
    Database.connect(ds)
    transaction {
        addLogger(StdOutSqlLogger)
        if (testing) SchemaUtils.drop(Users, Ads)
        SchemaUtils.create(Users, Ads)
    }
}

private fun setDBConfigFromEnv(config: HikariConfig) {
    fun setDsProps(k: String, v: String) = config.dataSourceProperties.setProperty(k, v)
    fun getDBConfEnv(): Map<String, String> {
        return System.getenv()
            .filter {
                listOf("DB_USER", "DB_PASS", "DB_HOST", "DB_PORT", "DB_NAME").contains(it.key) && it.value.isNotEmpty()
            }
    }

    getDBConfEnv().forEach {
        when (it.key) {
            "DB_USER" -> setDsProps("user", it.value)
            "DB_PASS" -> setDsProps("password", it.value)
            "DB_HOST" -> setDsProps("serverName", it.value)
            "DB_PORT" -> setDsProps("portNumber", it.value)
            "DB_NAME" -> setDsProps("databaseName", it.value)
        }
    }
}
