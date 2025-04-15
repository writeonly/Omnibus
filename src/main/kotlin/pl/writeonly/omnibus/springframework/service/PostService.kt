package pl.writeonly.omnibus.springframework.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import pl.writeonly.omnibus.springframework.model.Post
import pl.writeonly.omnibus.springframework.repository.PostRepository

@Service
class PostService(val repository: PostRepository) {

    fun getPosts(pageNumber0: Int): List<Post> = run {
        val pageNumber = if (0 <= pageNumber0) pageNumber0 else 0
        repository.findAllPost(PageRequest.of(pageNumber, pageSize))
    }

    fun getPost(id: Long): Post = repository.getReferenceById(id)

    fun findAllByTitle(title: String): List<Post> =
        repository.findAllByTitle(title)

    private companion object {
        const val pageSize = 10
    }
}
