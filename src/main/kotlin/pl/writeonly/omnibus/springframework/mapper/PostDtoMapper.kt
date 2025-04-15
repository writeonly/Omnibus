package pl.writeonly.omnibus.springframework.mapper

import pl.writeonly.omnibus.springframework.dto.PostDto
import pl.writeonly.omnibus.springframework.model.Post

class PostDtoMapper

fun mapToPostDtos(posts: List<Post>) = posts.map {
    mapToPostDto(it)
}

fun mapToPostDto(post: Post) = PostDto(
    id = post.id,
    title = post.title,
    content = post.content,
    created = post.created
)
