package pl.writeonly.omnibus.auth.registration

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

@Component
class KeycloakAdminClient(
    private val properties: KeycloakProperties,
) {
    private val restClient: RestClient = RestClient.builder()
        .baseUrl(properties.url.trimEnd('/'))
        .build()

    fun createUser(event: UserRegisteredEvent) {
        val token = adminToken()
        val body = mapOf(
            "username" to event.username,
            "email" to event.email,
            "firstName" to event.firstName,
            "lastName" to event.lastName,
            "enabled" to true,
            "emailVerified" to false,
            "credentials" to listOf(
                mapOf(
                    "type" to "password",
                    "value" to event.password,
                    "temporary" to false,
                ),
            ),
            "attributes" to mapOf("omnibusUserId" to listOf(event.userId)),
        )

        try {
            restClient.post()
                .uri("/admin/realms/{realm}/users", properties.realm)
                .headers { it.setBearerAuth(token) }
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity()
        } catch (exception: HttpClientErrorException.Conflict) {
            // Keycloak already has this account; the event is idempotent enough for retries.
        }
    }

    private fun adminToken(): String {
        val form = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "password")
            add("client_id", properties.adminClientId)
            add("username", properties.adminUsername)
            add("password", properties.adminPassword)
        }

        val response = restClient.post()
            .uri("/realms/master/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(TokenResponse::class.java)

        return requireNotNull(response?.accessToken) { "Keycloak admin token response did not contain access_token" }
    }

    data class TokenResponse(
        @com.fasterxml.jackson.annotation.JsonProperty("access_token")
        val accessToken: String? = null,
    )
}
