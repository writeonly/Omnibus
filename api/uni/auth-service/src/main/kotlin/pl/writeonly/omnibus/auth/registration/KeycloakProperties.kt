package pl.writeonly.omnibus.auth.registration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "omnibus.keycloak")
data class KeycloakProperties(
    val url: String,
    val realm: String,
    val adminUsername: String,
    val adminPassword: String,
    val adminClientId: String = "admin-cli",
    val frontendClientId: String = "omnibus-frontend",
)
