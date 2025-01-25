package com.example.socialconnect.message.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialconnect.ChatViewModel
import com.example.socialconnect.Message
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(
    navController: NavHostController,
    currentUserId: String,
    otherUserId: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatRoomId by viewModel.chatRoomId.collectAsState()
    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(otherUserId) {
        viewModel.getOrCreateChatRoom(currentUserId, otherUserId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Messages List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Text(
                    text = message.text,
                    modifier = Modifier.padding(8.dp),
                    style = if (message.senderId == currentUserId) {
                        TextStyle(color = Color.Blue)
                    } else {
                        TextStyle(color = Color.Black)
                    }
                )
            }
        }

        // Message Input
        var messageText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )
            IconButton(onClick = {
                if (chatRoomId != null && messageText.isNotBlank()) {
                    viewModel.sendMessage(
                        chatRoomId = chatRoomId!!,
                        message = Message(
                            senderId = currentUserId,
                            text = messageText
                        )
                    )
                    messageText = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = null)
            }
        }
    }
}
