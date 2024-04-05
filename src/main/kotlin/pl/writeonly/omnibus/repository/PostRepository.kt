package pl.writeonly.omnibus.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.writeonly.omnibus.model.Post

@Repository
interface PostRepository : JpaRepository<Post, Long> {

  //    @Query("SELECT p FROM Post p left join fetch p.comments")
  @Query("SELECT p FROM Post p")
  fun findAllPost(pageable: Pageable): List<Post>

  fun findAllByTitle(title: String): List<Post>

}