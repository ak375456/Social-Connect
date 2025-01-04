package com.example.socialconnect.post_feature.domain.model

data class Post(
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val likesCount: Int = 0,
    val commentsCount: Int = 0
)
