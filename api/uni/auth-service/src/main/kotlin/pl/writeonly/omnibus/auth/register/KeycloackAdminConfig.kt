package pl.writeonly.omnibus.auth.register

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeycloakAdminConfig(
    @Value("\${keycloak.server-url}")
    private val serverUrl: String,

    @Value("\${keycloak.realm}")
    private val realm: String,

    @Value("\${keycloak.admin.client-id}")
    private val clientId: String,

    @Value("\${keycloak.admin.client-secret}")
    private val clientSecret: String,
) {

    @Bean
    fun keycloakAdmin(): Keycloak =
        KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm("master")
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build()
}