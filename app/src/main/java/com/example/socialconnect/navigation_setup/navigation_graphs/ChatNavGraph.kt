package com.example.socialconnect.navigation_setup.navigation_graphs

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialconnect.message.presentation.ChatScreen
import com.example.socialconnect.navigation_setup.CHAT_ROUTE
import com.example.socialconnect.navigation_setup.ChatScreen

@Composable
fun ChatNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ChatScreen.ChatListScreen.route,
        route = CHAT_ROUTE
    ){
        composable(
            route = ChatScreen.ChatListScreen.route){
            ChatScreen(navController)
        }
    }
}