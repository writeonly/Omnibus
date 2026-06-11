package pl.writeonly.omnibus.auth.features.logout

data class LogoutRequest(
    val refreshToken: String,
)