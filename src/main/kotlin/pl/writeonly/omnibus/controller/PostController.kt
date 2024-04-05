package pl.writeonly.omnibus.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.model.Post
import pl.writeonly.omnibus.service.PostService

@Tag(name = "Post", description = "the Post Api")
@RestController
class PostController(val service: PostService) {

    @GetMapping("/posts")
    fun getPosts(): List<Post> = service.getPosts()

    @GetMapping("/posts/{id}")
    fun getPost(id: Long): Post = service.getPost(id)
}