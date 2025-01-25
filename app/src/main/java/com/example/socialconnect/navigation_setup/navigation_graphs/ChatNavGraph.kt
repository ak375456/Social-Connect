package com.example.socialconnect.navigation_setup.navigation_graphs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialconnect.authentication.presentation.AuthViewModel
import com.example.socialconnect.message.presentation.ChatScreen
import com.example.socialconnect.navigation_setup.CHAT_ROUTE
import com.example.socialconnect.navigation_setup.ChatScreen

@Composable
fun ChatNavGraph(
    navController: NavHostController,
    otherUserId: String // Receive parameter from home
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUserId = authViewModel.getCurrentUserId().orEmpty()

    NavHost(
        navController = navController,
        startDestination = "chat/$otherUserId", // Use dynamic start destination
        route = "$CHAT_ROUTE/$otherUserId" // Unique route for each chat
    ) {
        composable(
            route = "chat/{otherUserId}"
        ) { backStackEntry ->
            val finalOtherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
            ChatScreen(
                navController = navController,
                currentUserId = currentUserId,
                otherUserId = finalOtherUserId
            )
        }
    }
}