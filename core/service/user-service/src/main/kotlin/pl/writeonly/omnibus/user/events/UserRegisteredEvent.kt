package pl.writeonly.omnibus.user.events

data class UserRegisteredEvent(
    val userId: String,
    val username: String,
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
)
