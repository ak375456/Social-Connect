package com.example.socialconnect.navigation_setup.navigation_graphs



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialconnect.home.presentation.ProfileScreen
import com.example.socialconnect.home.presentation.RealHomeScreen
import com.example.socialconnect.home.presentation.SettingScreen
import com.example.socialconnect.navigation_setup.BottomAppBarScreen
import com.example.socialconnect.navigation_setup.HOME_ROUTE
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
    }
}
