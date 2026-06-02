package pl.writeonly.omnibus.auth.controller
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.auth.login.KeycloakLoginClient
import pl.writeonly.omnibus.auth.login.LoginRequest
import pl.writeonly.omnibus.auth.login.LoginResponse
import pl.writeonly.omnibus.auth.service.JwtBlacklistService
import java.time.Instant

@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtDecoder: JwtDecoder,
    private val blacklistService: JwtBlacklistService,
    private val keycloakLoginClient: KeycloakLoginClient,
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse =
        keycloakLoginClient.login(request)

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") auth: String) {

        val token = auth.removePrefix("Bearer ").trim()
        val jwt = jwtDecoder.decode(token)

        val jti = jwt.id
        val expiresAt = jwt.expiresAt?.epochSecond ?: 0
        val now = Instant.now().epochSecond

        val ttl = expiresAt - now

        if (jti != null && ttl > 0) {
            blacklistService.revoke(jti, ttl)
        }
    }
}
