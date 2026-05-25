
package pl.writeonly.omnibus.gateway.controller
import org.springframework.web.bind.annotation.*
import pl.writeonly.omnibus.grpc.user.v1.*
import pl.writeonly.omnibus.gateway.service.user.RegisterResponseDto
import pl.writeonly.omnibus.gateway.service.user.RegisterRequestDto
import net.devh.boot.grpc.client.inject.GrpcClient



@RestController
@RequestMapping("/api/users")
class UserGatewayController(
    @GrpcClient("userService")
    private val userStub: UserServiceGrpc.UserServiceBlockingStub
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequestDto): RegisterResponseDto {

        val grpcRequest = RegisterUserRequest.newBuilder()
            .setUsername(req.username)
            .setEmail(req.email)
            .setPassword(req.password)
            .setFirstName(req.firstName)
            .setLastName(req.lastName)
            .build()

        val response = userStub.registerUser(grpcRequest)

        return RegisterResponseDto(
            userId = response.userId,
            username = response.username,
            email = response.email,
            status = response.status
        )
    }
}



