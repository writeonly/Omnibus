package pl.writeonly.omnibus.auth.features.login

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String?,
    val tokenType: String,
    val expiresIn: Long?,
)
