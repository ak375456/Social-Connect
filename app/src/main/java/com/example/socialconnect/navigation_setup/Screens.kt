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

sealed class Screens(val route:String) {
    data object LoginScreen : Screens("loginScreen")
    data object SignupScreen : Screens("signupScreen")
    data object ForgetPasswordScreen : Screens("forgetPasswordScreen")
}

sealed class BottomAppBarScreen(val route: String, val label: String, val icon: ImageVector) {
    object HomeScreen : BottomAppBarScreen(route = "homeScreen", label = "Home", icon = Icons.Default.Home)
    object ProfileScreen : BottomAppBarScreen("profileScreen", label = "Profile", icon = Lucide.PersonStanding)
    object SettingScreen : BottomAppBarScreen("SettingScreen", label = "Setting", icon = Icons.Default.Settings)
}