package pl.writeonly.omnibus.auth.features.register

import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class KeycloakRegisterService(
    private val keycloak: Keycloak,
    private val eventPublisher: org.springframework.context.ApplicationEventPublisher,
    @Value("\${keycloak.realm}")
    private val realm: String,
) {

    fun register(request: RegisterRequest) {

        val user = UserRepresentation().apply {
            username = request.username
            email = request.email
            firstName = request.firstName
            lastName = request.lastName
            isEnabled = true
        }

        keycloak.realm(realm).users().create(user).use { response ->
            if (response.status !in 200..299) {
                throw IllegalStateException("Cannot create user")
            }

            eventPublisher.publishEvent(
                UserRegisteredEvent(
                    username = request.username,
                    email = request.email,
                    firstName = request.firstName,
                    lastName = request.lastName
                )
            )
        }
    }
}
