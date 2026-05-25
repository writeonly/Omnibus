package pl.writeonly.omnibus.gateway.service.user

data class RegisterRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)
