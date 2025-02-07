package com.example.socialconnect.home.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialconnect.authentication.presentation.AuthState
import com.example.socialconnect.authentication.presentation.AuthViewModel
import com.example.socialconnect.navigation_setup.AUTH_ROUTE
import com.example.socialconnect.util.MyButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.composables.icons.lucide.Meh
import com.composables.icons.lucide.Menu
import com.composables.icons.lucide.MessageCircle
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.ThumbsUp
import com.example.socialconnect.home.presentation.utility.OptionMenu
import com.example.socialconnect.navigation_setup.ROOT_ROUTE
import com.example.socialconnect.post_feature.presentation.PostState
import com.example.socialconnect.post_feature.presentation.PostViewModel
import com.example.socialconnect.post_feature.presentation.PostWithUser
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.socialconnect.navigation_setup.CHAT_ROUTE
import com.example.socialconnect.navigation_setup.Screens
import com.example.socialconnect.util.MyTextField


@Composable
fun RealHomeScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()

    val postState by postViewModel.postState.collectAsState()

    // Add search state
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Filtered posts based on search
    val filteredPosts = when (postState) {
        is PostState.Success -> {
            val posts = (postState as PostState.Success).posts
            if (searchQuery.isBlank()) {
                posts
            } else {
                posts.filter {
                    it.userName.contains(searchQuery, ignoreCase = true)
                }
            }
        }
        else -> emptyList()
    }

    // Show search dialog
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = { Text("Search Posts") },
            text = {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search by username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showSearchDialog = false }) {
                    Text("Close")
                }
            }
        )
    }


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

    Column(modifier = Modifier.fillMaxSize()) {
        when (postState) {
            is PostState.Success -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.weight(0.9f),
                        onClick = { authViewModel.signOut() }
                    ) {
                        Text("Sign Out")
                    }

                    // Update search icon click
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(Lucide.Search, "Search")
                    }
                }

                val currentUserId = authViewModel.getCurrentUserId().orEmpty()
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredPosts) { postWithUser ->
                        PostItem(
                            postWithUser = postWithUser,
                            currentUserId = currentUserId,
                            postViewModel = postViewModel,
                            onLikeClick = {
                                postViewModel.likePostOptimistic(postWithUser)
                            },
                            navController = navController
                        )
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
fun PostItem(
    postWithUser: PostWithUser,
    currentUserId: String,
    postViewModel: PostViewModel,
    onLikeClick: () -> Unit,
    navController: NavHostController,
) {
    var isFollowing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }
    var showTextFieldForEdit by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(postWithUser.post.content) }


    LaunchedEffect(Unit) {
        isFollowing = postViewModel.isFollowing(currentUserId, postWithUser.post.userId)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = Color.Gray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp)
                .clickable {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "selectedUserId", postWithUser.post.userId
                    )
                    navController.navigate(route = "${Screens.ViewProfileScreen.route}/${postWithUser.post.userId}")
                },
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

            // Follow/Unfollow button
            if (postWithUser.post.userId != currentUserId) {
                Spacer(modifier = Modifier.width(8.dp))

                MyButton(
                    text = if (isFollowing) "Unfollow" else "+ Follow",
                    isEmptyBackground = false,
                    onClick = {
                        if (isFollowing) {
                            postViewModel.unfollowUser(currentUserId, postWithUser.post.userId)
                        } else {
                            postViewModel.followUser(currentUserId, postWithUser.post.userId)
                        }
                        isFollowing = !isFollowing
                    }
                )

            }
        }
        if (showTextFieldForEdit) {
            MyTextField(
                value = editedText,
                onValueChanged = {
                    editedText = it
                },

                label = "",
                painter = Lucide.Meh,
                isPassword = false,
                isError = false,
                supportingTextFunction = {
                    Text("Edit Post")
                },
                isReadOnly = false
            )
            MyButton(
                modifier = Modifier.weight(0.5f),
                onClick = {
                    val updatedPost = postWithUser.post.copy(content = editedText)
                    postViewModel.updatePost(updatedPost)
                },
                text = "Edit!"
            )
        } else {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = postWithUser.post.content
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(onClick = {
                    onLikeClick()
                }) {
                    Icon(Lucide.ThumbsUp, "")
                }
            }
            Text(postWithUser.post.likesCount.toString())
            if (postWithUser.post.userId != currentUserId) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        navController.navigate("$CHAT_ROUTE/${postWithUser.post.userId}")
                    }) {
                        Icon(Lucide.MessageCircle, "")

                    }
                }
            }
            if (postWithUser.post.userId == currentUserId) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box {
                        IconButton(
                            onClick = { showMenu = true }
                        ) {
                            Icon(Lucide.Menu, "Options")
                        }
                        OptionMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            onEditClick = {
                                showTextFieldForEdit = true
//                                val updatedPost = postWithUser.post.copy(
//                                    content = "UPDATED POST",
//                                    timestamp = System.currentTimeMillis()
//                                )
//                                postViewModel.updatePost(updatedPost)
                            },
                            onDeleteClick = {
                                postViewModel.deletePost(postWithUser.post.id)
                                Log.d("MINE", "post delete")
                            }
                        )
                    }
                }
            }
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


