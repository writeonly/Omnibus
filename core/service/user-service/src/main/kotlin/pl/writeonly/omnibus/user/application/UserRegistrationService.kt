package pl.writeonly.omnibus.user.application

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.writeonly.omnibus.user.domain.UserAccount
import pl.writeonly.omnibus.user.domain.UserRegistrationRequest
import pl.writeonly.omnibus.user.domain.UserRegistrationResponse
import pl.writeonly.omnibus.user.events.OutboxEvent
import pl.writeonly.omnibus.user.events.OutboxEventRepository
import java.util.UUID

@Service
class UserRegistrationService(
    private val userAccountRepository: UserAccountRepository,
    private val outboxEventRepository: OutboxEventRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun register(request: UserRegistrationRequest): UserRegistrationResponse {
        val username = request.username.trim()
        val email = request.email.trim().lowercase()

        require(username.isNotBlank()) { "username is required" }
        require(email.isNotBlank()) { "email is required" }
        require(request.password.length >= MIN_PASSWORD_LENGTH) {
            "password must contain at least $MIN_PASSWORD_LENGTH characters"
        }
        require(!userAccountRepository.existsByUsernameIgnoreCase(username)) {
            "username already exists"
        }
        require(!userAccountRepository.existsByEmailIgnoreCase(email)) {
            "email already exists"
        }

        val account = userAccountRepository.save(
            UserAccount(
                id = UUID.randomUUID(),
                username = username,
                email = email,
                passwordHash = passwordEncoder.encode(request.password),
                firstName = request.firstName?.trim()?.ifBlank { null },
                lastName = request.lastName?.trim()?.ifBlank { null },
            ),
        )

        outboxEventRepository.save(
            OutboxEvent(
                aggregateType = "user",
                aggregateId = account.id.toString(),
                eventType = USER_REGISTERED_EVENT_TYPE,
                payload = mapOf(
                    "userId" to account.id.toString(),
                    "username" to account.username,
                    "email" to account.email,
                    "password" to request.password,
                    "firstName" to account.firstName,
                    "lastName" to account.lastName,
                ),
            ),
        )

        return UserRegistrationResponse(
            userId = account.id,
            username = account.username,
            email = account.email,
            status = account.status,
        )
    }

    companion object {
        const val USER_REGISTERED_EVENT_TYPE = "user.registered"
        private const val MIN_PASSWORD_LENGTH = 8
    }
}
