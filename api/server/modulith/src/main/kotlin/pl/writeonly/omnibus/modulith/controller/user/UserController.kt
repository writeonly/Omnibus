package pl.writeonly.omnibus.modulith.controller.user

import org.springframework.web.bind.annotation.*
import pl.writeonly.omnibus.grpc.user.v1.*
import net.devh.boot.grpc.client.inject.GrpcClient
import pl.writeonly.omnibus.modulith.controller.grpcMono
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user/users")
class UserController(
    @GrpcClient("userService")
    private val stub: UserServiceGrpc.UserServiceStub
) {

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterUserRequest): Mono<RegisterUserResponse> {
        return grpcMono { observer ->
            stub.registerUser(body, observer)
        }
    }
}



