package pl.writeonly.omnibus.user.grpc

import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import pl.writeonly.omnibus.grpc.user.v1.RegisterUserRequest
import pl.writeonly.omnibus.grpc.user.v1.RegisterUserResponse
import pl.writeonly.omnibus.grpc.user.v1.UserServiceGrpc
import pl.writeonly.omnibus.user.application.UserRegistrationService

@GrpcService
class UserGrpcService(
    private val userRegistrationService: UserRegistrationService,
) : UserServiceGrpc.UserServiceImplBase() {
    override fun registerUser(
        request: RegisterUserRequest,
        responseObserver: StreamObserver<RegisterUserResponse>,
    ) {
        responseObserver.completeWith {
            userRegistrationService.register(request.toDomain()).toGrpc()
        }
    }

    private fun <T> StreamObserver<T>.completeWith(block: () -> T) {
        try {
            onNext(block())
            onCompleted()
        } catch (exception: RuntimeException) {
            onError(exception)
        }
    }
}
