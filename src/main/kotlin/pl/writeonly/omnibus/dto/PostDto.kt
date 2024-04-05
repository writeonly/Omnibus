package pl.writeonly.omnibus.dto

import java.time.LocalDateTime

data class PostDto(
  val id: Long?,
  val title: String?,
  val content: String?,
  val created: LocalDateTime?,
)