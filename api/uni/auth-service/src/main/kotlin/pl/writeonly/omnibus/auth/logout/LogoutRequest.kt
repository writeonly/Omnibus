package pl.writeonly.omnibus.auth.logout

data class LogoutRequest(
    val refreshToken: String,
)