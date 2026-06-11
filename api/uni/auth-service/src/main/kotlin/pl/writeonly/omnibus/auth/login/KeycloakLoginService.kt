package pl.writeonly.omnibus.auth.login

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import pl.writeonly.omnibus.auth.register.KeycloakProperties

@Service
class KeycloakLoginService(
    private val properties: KeycloakProperties,
) {

    fun login(request: LoginRequest): LoginResponse {
        try {
            val keycloak = KeycloakBuilder.builder()
                .serverUrl(properties.url)
                .realm(properties.realm)
                .clientId(properties.frontendClientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(request.username)
                .password(request.password)
                .build()

            val token = keycloak.tokenManager().accessToken

            return LoginResponse(
                accessToken = token.token,
                refreshToken = token.refreshToken,
                tokenType = "Bearer",
                expiresIn = token.expiresIn,
            )
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password",
                e
            )
        }
    }
}