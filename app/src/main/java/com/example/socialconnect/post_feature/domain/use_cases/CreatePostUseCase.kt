package com.example.socialconnect.post_feature.domain.use_cases

import com.example.socialconnect.post_feature.domain.model.Post
import com.example.socialconnect.post_feature.domain.repository.PostRepository

class CreatePostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: Post) {
        repository.createPost(post)
    }
}