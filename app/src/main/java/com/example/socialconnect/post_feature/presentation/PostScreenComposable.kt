package com.example.socialconnect.post_feature.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PostScreenComposable(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    var postContent by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = postContent,
            onValueChange = { postContent = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Write your post...") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (postContent.isNotBlank()) {
                    postViewModel.createPost(postContent)
                    postContent = "" // Clear input after posting
                    Toast.makeText(context, "Post created successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Content cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post")
        }
    }
}
