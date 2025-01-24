package com.example.socialconnect.navigation_setup.navigation_graphs



import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socialconnect.home.presentation.ProfileScreen
import com.example.socialconnect.home.presentation.RealHomeScreen
import com.example.socialconnect.home.presentation.SettingScreen
import com.example.socialconnect.home.presentation.ViewProfile
import com.example.socialconnect.navigation_setup.BottomAppBarScreen
import com.example.socialconnect.navigation_setup.CHAT_ROUTE
import com.example.socialconnect.navigation_setup.HOME_ROUTE
import com.example.socialconnect.navigation_setup.Screens
import com.example.socialconnect.post_feature.presentation.PostScreenComposable

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomAppBarScreen.HomeScreen.route,
        route = HOME_ROUTE
    ){
        composable(route = BottomAppBarScreen.HomeScreen.route){
            RealHomeScreen(navController)
        }
        composable(route = BottomAppBarScreen.ProfileScreen.route){
            ProfileScreen()
    }
        composable(route = BottomAppBarScreen.SettingScreen.route){
            SettingScreen()
        }
        composable(route = Screens.PostScreen.route){
            PostScreenComposable(navController = navController)
        }
        composable(
            route = "${Screens.ViewProfileScreen.route}/{userId}"
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            Log.d("IDDD", userId.toString())
                ViewProfile(userId = userId.toString())
        }
        composable(route = CHAT_ROUTE){
            val chatNavController = rememberNavController()
            ChatNavGraph(chatNavController)
        }
    }
}
