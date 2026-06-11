package pl.writeonly.omnibus.auth

import org.junit.jupiter.api.*
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*  
import java.time.Duration

@Disabled("Requires Keycloak container setup, run manually")
@Testcontainers
class KeycloakE2ETest {

    companion object {

        @Container
        val keycloak = GenericContainer("keycloak/keycloak:25.0.6")
        .withExposedPorts(8080)
        .withEnv("KEYCLOAK_ADMIN", "kcadmin")
        .withEnv("KEYCLOAK_ADMIN_PASSWORD", "kcadmin")
        .withCommand("start-dev")
        .withStartupTimeout(Duration.ofMinutes(2))
    }

    private lateinit var adminClient: Keycloak
    private lateinit var restClient: RestClient
    private lateinit var baseUrl: String

    private val realm = "test-realm"
    private val clientId = "test-client"

    @BeforeEach
    fun setup() {

        baseUrl = "http://${keycloak.host}:${keycloak.firstMappedPort}"

        adminClient = KeycloakBuilder.builder()
            .serverUrl(baseUrl)
            .realm("master")
            .username("admin")
            .password("admin")
            .clientId("admin-cli")
            .build()

        // CREATE REALM
        val realmRep = org.keycloak.representations.idm.RealmRepresentation().apply {
            realm = this@KeycloakE2ETest.realm
            isEnabled = true
        }

        adminClient.realms().create(realmRep)

        // CREATE CLIENT
        val client = org.keycloak.representations.idm.ClientRepresentation().apply {
            this.clientId = clientId
            isPublicClient = true
            redirectUris = listOf("*")
        }

        adminClient.realm(realm)
            .clients()
            .create(client)

        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build()
    }

    @Test
    fun `register login logout flow`() {

        val username = "user-${UUID.randomUUID()}"
        val password = "password"

        createUser(username, password)

        val token = login(username, password)

        Assertions.assertNotNull(token.access_token)

        logout(token.refresh_token!!)

        Assertions.assertThrows(Exception::class.java) {
            refresh(token.refresh_token!!)
        }
    }

    // -------------------------
    // helpers
    // -------------------------

    private fun createUser(username: String, password: String) {

        val user = UserRepresentation().apply {
            this.username = username
            isEnabled = true
        }

        val response = adminClient.realm(realm)
            .users()
            .create(user)

        val userId = response.location.path.substringAfterLast("/")

        val cred = CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = password
            isTemporary = false
        }

        adminClient.realm(realm)
            .users()
            .get(userId)
            .resetPassword(cred)
    }

    private fun login(username: String, password: String): TokenResponse {
        return restClient.post()
            .uri("/realms/$realm/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                mapOf(
                    "grant_type" to "password",
                    "client_id" to clientId,
                    "username" to username,
                    "password" to password
                )
            )
            .retrieve()
            .body(TokenResponse::class.java)!!
    }

    private fun logout(refreshToken: String) {
        restClient.post()
            .uri("/realms/$realm/protocol/openid-connect/logout")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                mapOf(
                    "client_id" to clientId,
                    "refresh_token" to refreshToken
                )
            )
            .retrieve()
            .toBodilessEntity()
    }

    private fun refresh(refreshToken: String): TokenResponse {
        return restClient.post()
            .uri("/realms/$realm/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                mapOf(
                    "grant_type" to "refresh_token",
                    "client_id" to clientId,
                    "refresh_token" to refreshToken
                )
            )
            .retrieve()
            .body(TokenResponse::class.java)!!
    }

    data class TokenResponse(
        val access_token: String,
        val refresh_token: String? = null,
        val token_type: String? = null,
        val expires_in: Long? = null
    )
}
