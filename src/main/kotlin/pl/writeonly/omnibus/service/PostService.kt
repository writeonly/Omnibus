package pl.writeonly.omnibus.service

import org.springframework.stereotype.Service
import pl.writeonly.omnibus.model.Post
import pl.writeonly.omnibus.repository.PostRepository

@Service
class PostService(val repository: PostRepository) {

    fun getPosts(): List<Post> = repository.findAll()

    fun getPost(id: Long): Post = repository.getById(id)
}