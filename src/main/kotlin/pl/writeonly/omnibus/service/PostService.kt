package pl.writeonly.omnibus.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import pl.writeonly.omnibus.model.Post
import pl.writeonly.omnibus.repository.PostRepository

@Service
class PostService(val repository: PostRepository) {

  fun getPosts(pageNumber0: Int): List<Post> = run {
    val pageNumber = if (0 <= pageNumber0) pageNumber0 else 0
    repository.findAllPost(PageRequest.of(pageNumber, 10))
  }

  fun getPost(id: Long): Post = repository.getById(id)

  fun findAllByTitle(title: String): List<Post> =
    repository.findAllByTitle(title)
}