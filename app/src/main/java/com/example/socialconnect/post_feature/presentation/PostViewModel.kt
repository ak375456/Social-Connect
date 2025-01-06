package com.example.socialconnect.post_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialconnect.post_feature.domain.model.Post
import com.example.socialconnect.post_feature.domain.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {


    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> = _postState



    fun fetchPostsWithUserDetails() {
        viewModelScope.launch {
            _postState.value = PostState.Loading
            try {
                val posts = postRepository.getPosts()
                val postsWithUsers = posts.mapNotNull { post ->
                    val user = getUserById(post.userId) // Fetch user details here
                    user?.let {
                        PostWithUser(
                            post = post,
                            userName = "${it.firstName} ${it.lastName}",
                            userProfilePicture = it.profilePictureLink
                        )
                    }
                }
                _postState.value = PostState.Success(postsWithUsers)
            } catch (e: Exception) {
                _postState.value = PostState.Error(e.message ?: "Failed to fetch posts")
            }
        }
    }

    suspend fun getUserById(userId: String): User? {
        return try {
            val documentSnapshot = firestore.collection("users").document(userId).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun likePostOptimistic(postWithUser: PostWithUser) {

        val currentState = _postState.value
        if (currentState is PostState.Success) {
            val updatedPosts = currentState.posts.map {
                if (it.post.id == postWithUser.post.id) {
                    it.copy(post = it.post.copy(likesCount = it.post.likesCount + 1))
                } else {
                    it
                }
            }
            _postState.value = PostState.Success(updatedPosts)

            viewModelScope.launch {
                try {
                    val updatedPost = postWithUser.post.copy(likesCount = postWithUser.post.likesCount + 1)
                    postRepository.updatePost(updatedPost)
                } catch (e: Exception) {
                    // Rollback optimistic update on failure
                    _postState.value = PostState.Success(
                        currentState.posts.map {
                            if (it.post.id == postWithUser.post.id) {
                                it.copy(post = it.post.copy(likesCount = it.post.likesCount - 1))
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        }
    }

    fun createPost(content: String) {
        viewModelScope.launch {
            _postState.value = PostState.Loading
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val newPost = Post(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        content = content,
                        timestamp = System.currentTimeMillis(),
                        likesCount = 0,
                        commentsCount = 0
                    )
                    postRepository.createPost(newPost)
                    fetchPostsWithUserDetails() // Refresh posts
                } else {
                    _postState.value = PostState.Error("User is not authenticated")
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error(e.message ?: "Failed to create post")
            }
        }
    }
}


data class PostWithUser(
    val post: Post,
    val userName: String,
    val userProfilePicture: String
)

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profilePictureLink: String = "",
    val bio: String = "",
    val designation: String = "",
    val twitter: String = "",
    val website: String = ""
)


sealed class PostState {
    object Idle : PostState()
    object Loading : PostState()
    data class Success(val posts: List<PostWithUser>) : PostState()
    data class Error(val message: String) : PostState()
}


