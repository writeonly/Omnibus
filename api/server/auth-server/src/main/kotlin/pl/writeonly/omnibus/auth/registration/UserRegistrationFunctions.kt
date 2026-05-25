package pl.writeonly.omnibus.auth.registration

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class UserRegistrationFunctions(
    private val keycloakAdminClient: KeycloakAdminClient,
) {
    @Bean
    fun userRegistered(): Consumer<UserRegisteredEvent> =
        Consumer { event -> keycloakAdminClient.createUser(event) }
}
