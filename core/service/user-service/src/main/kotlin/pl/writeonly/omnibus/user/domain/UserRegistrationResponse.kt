package pl.writeonly.omnibus.user.domain

import java.util.UUID

data class UserRegistrationResponse(
    val userId: UUID,
    val username: String,
    val email: String,
    val status: String,
)
