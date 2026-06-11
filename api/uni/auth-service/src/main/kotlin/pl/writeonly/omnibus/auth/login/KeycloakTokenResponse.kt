package pl.writeonly.omnibus.auth.login
import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakTokenResponse(
    @field:JsonProperty("access_token")
    val accessToken: String? = null,
    @field:JsonProperty("refresh_token")
    val refreshToken: String? = null,
    @field:JsonProperty("token_type")
    val tokenType: String? = null,
    @field:JsonProperty("expires_in")
    val expiresIn: Long? = null,
)
