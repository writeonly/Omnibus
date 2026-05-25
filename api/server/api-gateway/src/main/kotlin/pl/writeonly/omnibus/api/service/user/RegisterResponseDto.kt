package pl.writeonly.omnibus.api.service.user

data class RegisterResponseDto(
    val userId: String,
    val username: String,
    val email: String,
    val status: String
)
