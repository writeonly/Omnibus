package pl.writeonly.omnibus.auth.features.login

data class LoginRequest(
    val username: String,
    val password: String,
)