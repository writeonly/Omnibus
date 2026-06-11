package pl.writeonly.omnibus.auth.features.register

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KeycloakProperties::class)
class RegistrationConfiguration
