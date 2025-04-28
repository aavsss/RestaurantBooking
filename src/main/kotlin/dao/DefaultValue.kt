package dao

import model.Guest
import java.util.*

object DefaultValue {
    val unsignedGuest = Guest(
        id = UUID.randomUUID(),
        firstName = "Unsigned",
        lastName = "Guest",
        phoneNumber = "(000) 000 - 0000",
    )
}
