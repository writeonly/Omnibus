package pl.writeonly.omnibus.auth.logout

import com.nimbusds.jwt.SignedJWT
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.auth.logout.KeycloakLogoutClient
import pl.writeonly.omnibus.auth.service.JwtBlacklistService
import java.time.Instant

@RestController()
@RequestMapping("/auth/logout")
class LogoutController(
    private val blacklistService: JwtBlacklistService,
    private val keycloakLogoutClient: KeycloakLogoutClient,
) {

    @PostMapping("")
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



