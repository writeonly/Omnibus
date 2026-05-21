package pl.writeonly.omnibus.user.grpc

import pl.writeonly.omnibus.grpc.user.v1.RegisterUserRequest
import pl.writeonly.omnibus.grpc.user.v1.RegisterUserResponse
import pl.writeonly.omnibus.user.domain.UserRegistrationRequest
import pl.writeonly.omnibus.user.domain.UserRegistrationResponse

fun RegisterUserRequest.toDomain(): UserRegistrationRequest =
    UserRegistrationRequest(
        username = username,
        email = email,
        password = password,
        firstName = firstName,
        lastName = lastName,
    )

fun UserRegistrationResponse.toGrpc(): RegisterUserResponse =
    RegisterUserResponse.newBuilder()
        .setUserId(userId.toString())
        .setUsername(username)
        .setEmail(email)
        .setStatus(status)
        .build()
