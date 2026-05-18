package pl.writeonly.omnibus.auth.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Configuration
class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {

        val authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer()

        http
            .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            .with(authorizationServerConfigurer, Customizer.withDefaults())
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }

        return http.build()
    }

    @Bean
    fun defaultSecurityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {

        http
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .formLogin(Customizer.withDefaults())

        return http.build()
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {

        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("api-client")
            .clientSecret(passwordEncoder().encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope("read")
            .scope("write")
            .build()

        return InMemoryRegisteredClientRepository(registeredClient)
    }

    @Bean
    fun userDetailsService(): UserDetailsService {

        val user = User.withUsername("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build()

        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {

        val keyPair = generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()

        val jwkSet = JWKSet(rsaKey)

        return ImmutableJWKSet(jwkSet)
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder =
        OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

    private fun generateRsaKey(): KeyPair {

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")

        keyPairGenerator.initialize(2048)

        return keyPairGenerator.generateKeyPair()
    }
}
