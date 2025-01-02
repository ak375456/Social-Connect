package com.example.socialconnect.authentication.presentation.user_repo

data class UserRepository(
    val firstName: String,
    val lastName:String,
    val id: String,
    val number: String,
    val profilePictureLink: String,
    val bio: String,
)
