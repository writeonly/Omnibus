package pl.writeonly.omnibus.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import java.time.LocalDateTime

@Entity
open class Post(
  @Id
  open var id: Long,
  open var title: String?,
  open var content: String?,
  open var created: LocalDateTime?,

  @OneToMany(targetEntity = Comment::class)
  @JoinColumn(name = "postId")
  open var comments: List<Comment>
)