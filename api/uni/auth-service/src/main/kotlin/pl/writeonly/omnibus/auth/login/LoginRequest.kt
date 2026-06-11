package pl.writeonly.omnibus.auth.login

data class LoginRequest(
    val username: String,
    val password: String,
)