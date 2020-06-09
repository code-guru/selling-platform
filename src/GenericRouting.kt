package lvls

import entity.DataEntity
import helpers.parseId
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import lvls.helpers.responseWithCreated
import lvls.helpers.responseWithDeleted
import lvls.helpers.responseWithError
import lvls.helpers.responseWithUpdated
import services.ApiService
import kotlin.reflect.KClass

fun <T : DataEntity> Routing.serviceRouting(
    typeName: String,
    typeClass: KClass<T>,
    validationFn: (entity: T) -> Unit,
    service: ApiService<T>
) {
    val typeNamePlural = "${typeName}s"

    route("/${typeNamePlural}") {
        get("/") {
            call.respond(mapOf(typeNamePlural to service.getAll(call.parameters)))
        }

        get("/{id}") {
            val id: Int = parseId(call.parameters["id"])
            val entity = service.findOne(id)
            if (entity != null) {
                call.respond(entity)
            } else
                responseWithError(
                    call,
                    "NotFound",
                    "The resource (${typeName} with id=$id) not found.",
                    HttpStatusCode.NotFound
                )
        }
        post("/") {
            val entity: T = call.receive(typeClass)
            validationFn(entity)
            val newEntityId: Int = service.insert(entity)
            responseWithCreated(call, "$typeName with id=$newEntityId")
        }

        put("/{id}") {
            val id: Int = parseId(call.parameters["id"])
            val entity: T = call.receive(typeClass)
            val result = service.update(entity, id)
            when {
                (result <= 0) -> responseWithError(
                    call,
                    "NonExisting", "$typeName with id=$id does not exists",
                    HttpStatusCode.NotAcceptable
                )
                else -> responseWithUpdated(call, "$typeName with id=$id")
            }


        }

        delete("/{id}") {
            val id: Int = parseId(call.parameters["id"])
            val result = service.delete(id)
            when {
                (result <= 0) -> responseWithError(
                    call,
                    "NonExisting", "$typeName with id=$id does not exists",
                    HttpStatusCode.NotAcceptable
                )
                else -> responseWithDeleted(call, "$typeName with id=$id")
            }
        }
    }
}
