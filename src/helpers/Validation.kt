package helpers

import entity.Ad
import entity.User
import io.ktor.features.BadRequestException
import lvls.InvalidRequestException
import java.time.LocalDate
import java.util.regex.Pattern


fun parseId(id: String?): Int {
    if (id == null) {
        throw InvalidRequestException("Id parameter is missing")
    }

    try {
        return Integer.parseInt(id)
    } catch (e: Exception) {
        throw BadRequestException("Parameter Id must be an integer.")
    }
}

fun validateUserParam(user: User) {
    if (!isEmailValid(user.email)) {
        throw InvalidRequestException("The user's email is invalid.")
    }
}

fun validateAdParam(ad: Ad) {
    if (ad.creationDate == null) ad.creationDate = LocalDate.now()
    if (ad.title.isEmpty()) throw InvalidRequestException("The ad's title cannot be empty.")
}

fun isEmailValid(email: String): Boolean {
    return Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}
