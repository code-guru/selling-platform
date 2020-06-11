package lvls

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import entity.Ad
import entity.User
import helpers.initDB
import helpers.validateAdParam
import helpers.validateUserParam
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.path
import io.ktor.routing.routing
import lvls.helpers.responseWithError
import org.slf4j.event.Level
import service.AdService
import service.UserService


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)

            dateFormat.isLenient = true
            registerModule(JavaTimeModule())
            registerModule(Jdk8Module())
            registerModule(ParameterNamesModule())

            deactivateDefaultTyping()
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        }
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(AutoHeadResponse)

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ConditionalHeaders)

    install(StatusPages) {
        exception<BadRequestException> { cause ->
            responseWithError(call, "BadRequest", cause.message, HttpStatusCode.BadRequest)
        }

        exception<JsonMappingException> { cause ->
            val reference = cause.pathReference?.split(".")?.last()
            responseWithError(
                call,
                "ParsingError(${cause::class.simpleName})",
                "The request body is either invalid or missing a required property ($reference).",
                HttpStatusCode.BadRequest
            )
        }

        exception<Throwable> { cause ->
            log.error(cause.message, cause)
            responseWithError(
                call,
                "ServerError",
                "${cause::class.simpleName}: ${cause.message}",
                HttpStatusCode.InternalServerError
            )
        }

        status(HttpStatusCode.NotFound) {
            responseWithError(call, "NotFound", "The request path is not valid.", HttpStatusCode.BadRequest)
        }
    }

    initDB(testing)
    val userService = UserService()
    val adService = AdService()


    routing {
        serviceRouting("user", User::class, ::validateUserParam, userService)
        serviceRouting("ad", Ad::class, ::validateAdParam, adService)
    }
}

class InvalidRequestException(message: String) : RuntimeException(message)
