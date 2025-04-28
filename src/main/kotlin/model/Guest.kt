package model

import java.util.UUID

data class Guest(
    val id: UUID,
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val phoneNumber: String,
    val email: String? = null
)
