package com.example.socialconnect.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialconnect.authentication.presentation.AuthState
import com.example.socialconnect.authentication.presentation.AuthViewModel
import com.example.socialconnect.home.BottomAppBarHolder
import com.example.socialconnect.navigation_setup.AUTH_ROUTE
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ThumbsUp
import com.example.socialconnect.navigation_setup.ROOT_ROUTE
import com.example.socialconnect.post_feature.domain.model.Post
import com.example.socialconnect.post_feature.presentation.PostState
import com.example.socialconnect.post_feature.presentation.PostViewModel
import com.example.socialconnect.post_feature.presentation.PostWithUser
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun RealHomeScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()

    val postState by postViewModel.postState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.popBackStack()
            navController.navigate(AUTH_ROUTE) {
                popUpTo(ROOT_ROUTE)
            }
        }
    }

    LaunchedEffect(Unit) {
        postViewModel.fetchPostsWithUserDetails()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (postState) {
            is PostState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator()
                }
            }

            is PostState.Success -> {
                MyButton(
                    text = "Logout",
                    isEmptyBackground = false,
                    onClick = {
                        authViewModel.signOut()
                    }
                )
                val posts = (postState as PostState.Success).posts
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(posts) { postWithUser ->
                        PostItem(postWithUser, onLikeClick = {
                            postViewModel.likePost(postWithUser.post) // Handle liking a post
                        })
                    }
                }
            }

            is PostState.Error -> {
                val errorMessage = (postState as PostState.Error).message
                Text(text = "Error: $errorMessage")
            }

            else -> {}
        }
    }
}

@Composable
fun PostItem(postWithUser: PostWithUser, onLikeClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = Color.Gray.copy(
                    alpha = 0.2f
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .clickable {},
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(postWithUser.userProfilePicture),
                    contentDescription = "User Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )

            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                postWithUser.userName, style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = postWithUser.post.content
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(onClick = onLikeClick) {
                Icon(Lucide.ThumbsUp, "")

            }
            Text(postWithUser.post.likesCount.toString())
        }
    }
    val formattedTime = formatTimestamp(postWithUser.post.timestamp)
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = "Post created at: $formattedTime",
        style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
    )
    HorizontalDivider()
}

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}


