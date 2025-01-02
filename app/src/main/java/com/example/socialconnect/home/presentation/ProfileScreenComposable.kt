package com.example.socialconnect.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.socialconnect.util.MyTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.socialconnect.authentication.presentation.AuthState
import com.example.socialconnect.authentication.presentation.AuthViewModel


@Composable
fun ProfileScreen(authViewModel: AuthViewModel = hiltViewModel()){

    val authState by authViewModel.authState.collectAsState()
    val userData by authViewModel.userData.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.getUserData()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                title = "User Profile",

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (authState) {
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }
                is AuthState.Error -> {
                    val errorMessage = (authState as AuthState.Error).message
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }
                is AuthState.Success -> {
                    if (userData != null) {
                        UserProfileContent(userData = userData!!)
                    } else {
                        Text(text = "No user data available")
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
fun UserProfileContent(userData: Map<String, Any>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        userData["profilePictureLink"]?.let { profilePictureLink ->
            Image(
                painter = rememberAsyncImagePainter(profilePictureLink),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text ="${userData["firstName"]} ${userData["lastName"]}",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
        Text(
            text = "${userData["designation"] ?: "N/A"}",
            style = TextStyle(
                fontWeight = FontWeight.ExtraLight,
                fontSize = 16.sp
            )
        )
        Text(text = "Bio: ${userData["bio"] ?: "N/A"}")
        Text(text = "Number: ${userData["number"] ?: "N/A"}")


        Text(text = "Twitter: ${userData["twitter"] ?: "N/A"}")
        Text(text = "Website: ${userData["website"] ?: "N/A"}")
        Text(text = "Email: ${userData["email"] ?: "N/A"}")
    }
}