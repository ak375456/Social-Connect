package com.example.socialconnect.message.presentation

import androidx.activity.SystemBarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialconnect.message.domain.repo.Message
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialconnect.home.presentation.formatTimestamp
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatScreen(
    navController: NavHostController,
    currentUserId: String,
    otherUserId: String,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val chatRoomId by viewModel.chatRoomId.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(otherUserId) {
        viewModel.getOrCreateChatRoom(currentUserId, otherUserId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Messages List
        LazyColumn(
            modifier = Modifier.weight(9f),
            reverseLayout = false
        ) {
            items(messages) { message ->
                val timestamp = message.timestamp
                val formattedTime = formatTimestampToTime(timestamp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = if (message.senderId == currentUserId)
                        Arrangement.End
                    else {
                        Arrangement.Start
                    }
                ) {
                    if (message.senderId == currentUserId) {
                        Box(
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(18)
                            )
                        ) {
                            Column {
                                Text(
                                    text = message.text,
                                    modifier = Modifier
                                        .padding(8.dp),
                                    fontSize = 18.sp
                                )
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(bottom = 2.dp, end = 4.dp),
                                    text = formattedTime, style = TextStyle(
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Light
                                    )
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.background(
                                color = if (!isSystemInDarkTheme()) {
                                    Color(0xFFF0DFBC)
                                } else {
                                    Color(0xFF6f5b3e)
                                },
                                shape = RoundedCornerShape(18)
                            )
                        ) {
                            Text(
                                text = message.text,
                                modifier = Modifier
                                    .padding(8.dp),
                                fontSize = 18.sp,
                                color = Color.Black

                            )
                        }
                    }
                }
            }
        }

        // Message Input
        var messageText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f, false),
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
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
fun Boxx() {
    Box(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(18)
        )
    ) {
        Text(
            text = "Hello",
            modifier = Modifier
                .padding(8.dp),
            style = TextStyle(color = Color.Black)
        )
    }
}

fun formatTimestampToTime(timestamp: Long): String {
    val date = timestamp
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault()) // 12-hour format with AM/PM
    return sdf.format(date)
}
