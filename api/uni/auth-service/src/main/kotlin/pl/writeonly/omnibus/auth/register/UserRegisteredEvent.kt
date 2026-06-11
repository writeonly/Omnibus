package pl.writeonly.omnibus.auth.register

data class UserRegisteredEvent(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
)
