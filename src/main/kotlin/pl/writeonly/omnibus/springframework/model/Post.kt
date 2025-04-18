package pl.writeonly.omnibus.springframework.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import java.time.LocalDateTime

@Entity
open class Post(
    @Id
    open var id: Long?,
    open var title: String?,
    open var content: String?,
    open var created: LocalDateTime?,

    @OneToMany(targetEntity = Comment::class)
    @JoinColumn(name = "post_id")
    open var comments: List<Comment>
)
