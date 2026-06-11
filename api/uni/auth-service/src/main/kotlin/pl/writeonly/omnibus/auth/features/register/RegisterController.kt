package pl.writeonly.omnibus.auth.features.register

import com.nimbusds.jwt.SignedJWT
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import pl.writeonly.omnibus.auth.features.register.KeycloakRegisterService
import pl.writeonly.omnibus.auth.features.register.RegisterRequest
import pl.writeonly.omnibus.auth.features.register.RegisterResponse


@RestController()
@RequestMapping("/auth/register")
class RegisterController(
    private val keycloakRegisterService: KeycloakRegisterService,
) {

    @PostMapping("")
    fun register(
        @RequestBody request: RegisterRequest,
    ): RegisterResponse {
        keycloakRegisterService.register(request)

        return RegisterResponse(
            status = "REGISTERED"
        )
    }

}
