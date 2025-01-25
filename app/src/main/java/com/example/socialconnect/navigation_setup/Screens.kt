package com.example.socialconnect.navigation_setup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PersonStanding

const val ROOT_ROUTE = "root"
const val AUTH_ROUTE = "auth"
const val HOME_ROUTE = "home"
const val CHAT_ROUTE = "chat_root"

sealed class Screens(val route:String) {
    data object LoginScreen : Screens("loginScreen")
    data object SignupScreen : Screens("signupScreen")
    data object ForgetPasswordScreen : Screens("forgetPasswordScreen")
    data object AdditionalInformationScreen : Screens("AdditionalInformationScreen")
    data object PostScreen : Screens("PostScreen")
    data object ViewProfileScreen : Screens("ViewProfileScreen")
}

sealed class BottomAppBarScreen(val route: String, val label: String, val icon: ImageVector) {
    object HomeScreen : BottomAppBarScreen(route = "homeScreen", label = "Home", icon = Icons.Default.Home)
    object ProfileScreen : BottomAppBarScreen("profileScreen", label = "Profile", icon = Lucide.PersonStanding)
    object SettingScreen : BottomAppBarScreen("SettingScreen", label = "Setting", icon = Icons.Default.Settings)
}

// Add this to your ChatScreen sealed class
sealed class ChatScreen(val route: String) {
    object ChatListScreen : ChatScreen("chatListScreen")
    object ChatDetailScreen : ChatScreen("chat/{otherUserId}") { // Add parameterized route
        fun createRoute(otherUserId: String) = "chat/$otherUserId"
    }
}