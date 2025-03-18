package pl.writeonly.omnibus.mapper

import pl.writeonly.omnibus.dto.PostDto
import pl.writeonly.omnibus.model.Post

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
