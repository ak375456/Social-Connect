package com.example.socialconnect.post_feature.data

import com.example.socialconnect.post_feature.domain.model.Post
import com.example.socialconnect.post_feature.domain.repository.PostRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : PostRepository {

    private val postsCollection = firestore.collection("posts")

    override suspend fun createPost(post: Post) {
        try {
            val postMap = mapOf(
                "id" to post.id,
                "userId" to post.userId,
                "content" to post.content,
                "timestamp" to post.timestamp,
                "likesCount" to post.likesCount,
                "commentsCount" to post.commentsCount
            )
            postsCollection.document(post.id).set(postMap).await()
        } catch (e: Exception) {
            throw Exception("Failed to create post: ${e.message}")
        }
    }

    override suspend fun getPosts(): List<Post> {
        return try {
            val snapshot = postsCollection.orderBy("timestamp").get().await()
            snapshot.documents.map { document ->
                document.toObject(Post::class.java) ?: throw Exception("Invalid post data")
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch posts: ${e.message}")
        }
    }

    override suspend fun updatePost(post: Post) {
        try {
            val postMap = mapOf(
                "content" to post.content,
                "timestamp" to post.timestamp,
                "likesCount" to post.likesCount,
                "commentsCount" to post.commentsCount
            )
            postsCollection.document(post.id).update(postMap).await()
        } catch (e: Exception) {
            throw Exception("Failed to update post: ${e.message}")
        }
    }


    override suspend fun deletePost(postId: String) {
        try {
            postsCollection.document(postId).delete().await()
        } catch (e: Exception) {
            throw Exception("Failed to delete post: ${e.message}")
        }
    }
}
