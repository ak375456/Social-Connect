package com.example.socialconnect.post_feature.domain.repository

import com.example.socialconnect.post_feature.domain.model.Post

interface PostRepository {
    suspend fun createPost(post: Post)
    suspend fun getPosts(): List<Post>
    suspend fun updatePost(post: Post)
    suspend fun deletePost(postId: String)
}