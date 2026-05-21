package pl.writeonly.omnibus.user.domain

data class UserRegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
)
