package pl.writeonly.omnibus.user.application

import org.springframework.data.jpa.repository.JpaRepository
import pl.writeonly.omnibus.user.domain.UserAccount
import java.util.UUID

interface UserAccountRepository : JpaRepository<UserAccount, UUID> {
    fun existsByUsernameIgnoreCase(username: String): Boolean
    fun existsByEmailIgnoreCase(email: String): Boolean
}
