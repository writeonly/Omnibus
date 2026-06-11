package pl.writeonly.omnibus.auth.registration
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
import pl.writeonly.omnibus.auth.registration.KeycloakRegisterClient
import pl.writeonly.omnibus.auth.registration.RegisterRequest
import pl.writeonly.omnibus.auth.registration.RegisterResponse


@RestController
@RequestMapping("/auth/register")
class AuthController(
    private val blacklistService: JwtBlacklistService,
    private val keycloakRegisterClient: KeycloakRegisterClient,
    private val keycloakLoginClient: KeycloakLoginClient,
    private val keycloakLogoutClient: KeycloakLogoutClient,
) {

    @PostMapping("")
    fun register(
        @RequestBody request: RegisterRequest,
    ): RegisterResponse {
        keycloakRegisterClient.register(request)

        return RegisterResponse(
            status = "REGISTERED"
        )
    }

}
