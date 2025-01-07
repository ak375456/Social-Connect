package com.example.socialconnect.home.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.socialconnect.post_feature.presentation.PostViewModel
import com.example.socialconnect.post_feature.presentation.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

//Fix Ui tomorrow for profile Screen

@Composable
fun ViewProfile(userId: String, postViewModel: PostViewModel = hiltViewModel()) {
    // this User is not from Firebase Authentication, it is a repository.
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(userId) {
        user = postViewModel.getUserById(userId)
    }

    if (user != null) {
        Text("User: ${user!!.firstName} ${user!!.lastName}")
        Text("User: ${user!!.email} ${user!!.profilePictureLink}")
    } else {
        Text("Loading profile...")
    }
}

