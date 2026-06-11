package pl.writeonly.omnibus.auth.logout

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import pl.writeonly.omnibus.auth.register.KeycloakProperties

@Component
class KeycloakLogoutClient(
    private val properties: KeycloakProperties,
) {
    private val restClient: RestClient = RestClient.builder()
        .baseUrl(properties.url.trimEnd('/'))
        .build()

    fun logout(refreshToken: String) {
        val form = LinkedMultiValueMap<String, String>().apply {
            add("client_id", properties.frontendClientId)
            add("refresh_token", refreshToken)
        }

        try {
            restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/logout", properties.realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .toBodilessEntity()
        } catch (exception: HttpClientErrorException.BadRequest) {
            // Treat logout as idempotent from our API perspective: an expired or already
            // invalidated refresh token should not prevent local access-token blacklisting.
        }
    }
}

