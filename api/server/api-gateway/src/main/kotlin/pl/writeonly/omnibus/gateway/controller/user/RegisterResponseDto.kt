package pl.writeonly.omnibus.gateway.controller.user

data class RegisterResponseDto(
    val userId: String,
    val username: String,
    val email: String,
    val status: String
)
