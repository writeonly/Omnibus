package pl.writeonly.omnibus.auth.login

import com.nimbusds.jwt.SignedJWT
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
@RequestMapping("/auth/login")
class LoginController(
    private val blacklistService: JwtBlacklistService,
    private val keycloakLoginClient: KeycloakLoginClient,
) {
    @PostMapping("")
    fun login(@RequestBody request: LoginRequest): LoginResponse =
        keycloakLoginClient.login(request)
}



