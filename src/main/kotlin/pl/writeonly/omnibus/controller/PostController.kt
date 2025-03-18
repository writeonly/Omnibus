package pl.writeonly.omnibus.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.dto.PostDto
import pl.writeonly.omnibus.mapper.mapToPostDtos
import pl.writeonly.omnibus.model.Post
import pl.writeonly.omnibus.service.PostService

@Tag(name = "Post", description = "the Post Api")
@RestController
class PostController(val service: PostService) {

    @GetMapping("/posts")
    fun getPosts(@Min(0) page: Int = 0): List<PostDto> =
        mapToPostDtos(service.getPosts(page))

    @GetMapping("/posts/{id}")
    fun getPost(@Min(0) id: Long): Post = service.getPost(id)

    @GetMapping("/posts/title/{title}")
    fun findAllByTitle(title: String): List<PostDto> =
        mapToPostDtos(service.findAllByTitle(title))
}
