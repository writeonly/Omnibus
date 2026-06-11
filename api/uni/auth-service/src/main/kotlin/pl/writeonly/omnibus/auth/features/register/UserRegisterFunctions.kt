package pl.writeonly.omnibus.auth.features.register

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class UserRegisterFunctions(
    private val keycloakAdminClient: KeycloakAdminClient,
) {
    @Bean
    fun userRegistered(): Consumer<UserRegisteredEvent> =
        Consumer { event -> keycloakAdminClient.createUser(event) }
}
