package pl.writeonly.omnibus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.writeonly.omnibus.model.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {


  fun findAllByPostIdIn(ids: List<Long>): List<Comment>

}