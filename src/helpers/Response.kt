package lvls.helpers

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

suspend fun responseWithError(
    call: ApplicationCall,
    type: String,
    msg: String?,
    status: HttpStatusCode = HttpStatusCode.InternalServerError
) {
    call.respond(
        status, mapOf(
            "success" to false,
            "error" to mapOf(
                "type" to type,
                "message" to (msg ?: "")
            )
        )
    )
}

suspend fun responseWithSuccess(call: ApplicationCall, msg: String, status: HttpStatusCode = HttpStatusCode.OK) {
    call.respond(
        status, mapOf(
            "success" to "true",
            "message" to msg

        )
    )
}

suspend fun responseWithUpdated(call: ApplicationCall, info: String = "") {
    responseWithSuccess(call, getResponseMessage("updated", info))
}

suspend fun responseWithCreated(call: ApplicationCall, info: String = "") {
    responseWithSuccess(call, getResponseMessage("created", info), HttpStatusCode.Created)
}

suspend fun responseWithDeleted(call: ApplicationCall, info: String = "") {
    responseWithSuccess(call, getResponseMessage("deleted", info))
}

fun getResponseMessage(action: String, info: String): String {
    return "The resource${if (info.isNotEmpty()) " ($info) " else " "}has been ${action}."
}
