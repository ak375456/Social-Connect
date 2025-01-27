package com.example.socialconnect.message.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialconnect.message.domain.repo.ChatRoom
import com.example.socialconnect.message.domain.repo.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _chatRoomId = MutableStateFlow<String?>(null)
    val chatRoomId: StateFlow<String?> = _chatRoomId

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun getOrCreateChatRoom(currentUserId: String, otherUserId: String) {
        viewModelScope.launch {
            val chatRoomsRef = firestore.collection("chatRooms")
            chatRoomsRef
                .whereArrayContains("participants", currentUserId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val chatRoom = snapshot.documents.find { doc ->
                        val participants = doc["participants"] as List<*>
                        participants.contains(otherUserId)
                    }

                    if (chatRoom != null) {
                        _chatRoomId.value = chatRoom.id
                        listenForMessages(chatRoom.id)
                    } else {
                        val newChatRoomRef = chatRoomsRef.document()
                        val chatRoom = ChatRoom(
                            id = newChatRoomRef.id,
                            participants = listOf(currentUserId, otherUserId)
                        )
                        newChatRoomRef.set(chatRoom).addOnSuccessListener {
                            _chatRoomId.value = chatRoom.id
                            listenForMessages(chatRoom.id)
                        }
                    }
                }
        }
    }

    private fun listenForMessages(chatRoomId: String) {
        firestore.collection("chatRooms")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val messages = snapshot?.documents?.map { doc ->
                    doc.toObject(Message::class.java)!!
                } ?: emptyList()
                _messages.value = messages
            }
    }

    fun sendMessage(chatRoomId: String, message: Message) {
        firestore.collection("chatRooms")
            .document(chatRoomId)
            .collection("messages")
            .add(message)
    }
}
