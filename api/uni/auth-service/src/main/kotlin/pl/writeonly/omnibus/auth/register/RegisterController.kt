package pl.writeonly.omnibus.auth.register

import com.nimbusds.jwt.SignedJWT
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.auth.service.JwtBlacklistService
import java.time.Instant
import pl.writeonly.omnibus.auth.register.KeycloakRegisterClient
import pl.writeonly.omnibus.auth.register.RegisterRequest
import pl.writeonly.omnibus.auth.register.RegisterResponse


@RestController()
@RequestMapping("/auth/register")
class RegisterController(
    private val blacklistService: JwtBlacklistService,
    private val keycloakRegisterClient: KeycloakRegisterClient,
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
