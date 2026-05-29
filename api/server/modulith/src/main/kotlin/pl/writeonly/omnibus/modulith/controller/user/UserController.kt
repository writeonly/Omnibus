package pl.writeonly.omnibus.modulith.controller.user

import pl.writeonly.omnibus.grpc.user.v1.*
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.modulith.controller.grpcMono
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user/users")
class UserController(
    @GrpcClient("userService")
    private val stub: UserServiceGrpc.UserServiceStub,
) {

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterUserHttpRequest): Mono<RegisterUserHttpResponse> {
        val grpcRequest = RegisterUserRequest.newBuilder()
            .setUsername(body.username)
            .setEmail(body.email)
            .setPassword(body.password)
            .setFirstName(body.firstName.orEmpty())
            .setLastName(body.lastName.orEmpty())
            .build()

        return grpcMono { observer ->
            stub.registerUser(grpcRequest, observer)
        }.map {
            RegisterUserHttpResponse(
                userId = it.userId,
                username = it.username,
                email = it.email,
                status = it.status,
            )
        }
    }
}

data class RegisterUserHttpRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
)

data class RegisterUserHttpResponse(
    val userId: String,
    val username: String,
    val email: String,
    val status: String,
)
