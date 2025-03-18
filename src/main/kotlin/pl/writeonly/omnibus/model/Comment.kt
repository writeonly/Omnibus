package pl.writeonly.omnibus.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
open class Comment(
    @Id
    open var id: Long?,
    open var content: String?,
    open var created: LocalDateTime?
)
