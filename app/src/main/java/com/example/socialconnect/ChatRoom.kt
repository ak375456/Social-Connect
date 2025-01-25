package com.example.socialconnect

data class ChatRoom(
    val id: String = "",
    val participants: List<String> = listOf(),
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Message(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
