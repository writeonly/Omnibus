package pl.writeonly.omnibus.auth.controller
import com.nimbusds.jwt.SignedJWT
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.auth.login.KeycloakLoginClient
import pl.writeonly.omnibus.auth.login.KeycloakLogoutClient
import pl.writeonly.omnibus.auth.login.LoginRequest
import pl.writeonly.omnibus.auth.login.LoginResponse
import pl.writeonly.omnibus.auth.service.JwtBlacklistService
import java.time.Instant

@RestController
@RequestMapping("/auth")
class AuthController(
    private val blacklistService: JwtBlacklistService,
    private val keycloakLoginClient: KeycloakLoginClient,
    private val keycloakLogoutClient: KeycloakLogoutClient,
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse =
        keycloakLoginClient.login(request)

    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") auth: String,
        @RequestBody request: LogoutRequest,
    ): LogoutResponse {
        keycloakLogoutClient.logout(request.refreshToken)

        val token = auth.removePrefix("Bearer ").trim()
        val claims = SignedJWT.parse(token).jwtClaimsSet

        val jti = claims.jwtid
        val expiresAt = claims.expirationTime?.toInstant()?.epochSecond ?: 0
        val now = Instant.now().epochSecond

        val ttl = expiresAt - now

        if (jti != null && ttl > 0) {
            blacklistService.revoke(jti, ttl)
        }

        return LogoutResponse(status = "LOGGED_OUT")
    }
}

data class LogoutRequest(
    val refreshToken: String,
)

data class LogoutResponse(
    val status: String,
)
