package pl.writeonly.omnibus.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "user_accounts")
class UserAccount(
    @Id
    val id: UUID,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String?,

    @Column(name = "first_name")
    val firstName: String?,

    @Column(name = "last_name")
    val lastName: String?,

    @Column(nullable = false)
    val status: String = "PENDING_AUTH_PROVISIONING",

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),
)
