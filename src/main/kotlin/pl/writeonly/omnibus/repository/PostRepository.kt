package pl.writeonly.omnibus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.writeonly.omnibus.model.Post

@Repository
interface PostRepository : JpaRepository<Post, Long> {
}