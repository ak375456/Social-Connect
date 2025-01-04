package com.example.socialconnect.post_feature.domain.use_cases

import com.example.socialconnect.post_feature.domain.model.Post
import com.example.socialconnect.post_feature.domain.repository.PostRepository

class UpdatePostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: Post) {
        repository.updatePost(post)
    }
}