package pl.writeonly.omnibus.auth.login

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import pl.writeonly.omnibus.auth.register.KeycloakProperties

@Component
class KeycloakLoginClient(
    private val properties: KeycloakProperties,
) {
    private val restClient: RestClient = RestClient.builder()
        .baseUrl(properties.url.trimEnd('/'))
        .build()

    fun login(request: LoginRequest): LoginResponse {
        val form = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "password")
            add("client_id", properties.frontendClientId)
            add("username", request.username)
            add("password", request.password)
        }

        return try {
            val response = restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", properties.realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(KeycloakTokenResponse::class.java)

            val token = requireNotNull(response?.accessToken) {
                "Keycloak token response did not contain access_token"
            }

            LoginResponse(
                accessToken = token,
                refreshToken = response.refreshToken,
                tokenType = response.tokenType ?: "Bearer",
                expiresIn = response.expiresIn,
            )
        } catch (exception: HttpClientErrorException.Unauthorized) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", exception)
        } catch (exception: HttpClientErrorException.BadRequest) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", exception)
        }
    }
}

data class LoginRequest(
    val username: String,
    val password: String,
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String?,
    val tokenType: String,
    val expiresIn: Long?,
)

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
