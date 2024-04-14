package pl.writeonly.omnibus.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import pl.writeonly.omnibus.model.Comment
import pl.writeonly.omnibus.model.Post
import pl.writeonly.omnibus.repository.CommentRepository
import pl.writeonly.omnibus.repository.PostRepository

@Service
class PostService(
  val repository: PostRepository,
  val commentRepository: CommentRepository,
) {

  fun getPosts(pageNumber0: Int): List<Post> = run {
    val pageNumber = normalizePageNumber(pageNumber0)
    repository.findAllPost(PageRequest.of(pageNumber, 10))
  }

  fun getPostsWithComments(pageNumber0: Int): List<Post> = run {
    val pageNumber = normalizePageNumber(pageNumber0)
    val posts = repository.findAllPost(PageRequest.of(pageNumber, 10))
    val ids = posts.map{it.id}
    val comments = commentRepository.findAllByPostIdIn(ids)
    posts.map{
      it.comments = extractComments(comments, it.id)
      it
    }
  }

  private fun extractComments(comments: List<Comment>, postId : Long) =
    comments.filter { it.postId == postId }

  fun getPost(id: Long): Post = repository.getById(id)

  fun findAllByTitle(title: String): List<Post> =
    repository.findAllByTitle(title)
}

fun normalizePageNumber(pageNumber0: Int) =
  if (0 <= pageNumber0) pageNumber0 else 0