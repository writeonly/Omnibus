package pl.writeonly.omnibus.auth.features.login

import com.nimbusds.jwt.SignedJWT
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.auth.features.login.KeycloakLoginService
import pl.writeonly.omnibus.auth.features.login.LoginRequest
import pl.writeonly.omnibus.auth.features.login.LoginResponse
import java.time.Instant

@RestController
@RequestMapping("/auth/login")
class LoginController(
    private val keycloakLoginService: KeycloakLoginService,
) {
    @PostMapping("")
    fun login(@RequestBody request: LoginRequest): LoginResponse =
        keycloakLoginService.login(request)
}



