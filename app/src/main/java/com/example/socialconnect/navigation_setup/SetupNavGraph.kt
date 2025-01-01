package com.example.socialconnect.navigation_setup

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialconnect.home.presentation.HomeScreen
import com.example.socialconnect.navigation_setup.navigation_graphs.authNavGraph
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SetupNavGraph(navController: NavHostController){

    val startDestination = if
            (FirebaseAuth.getInstance().currentUser != null)
        HOME_ROUTE
    else
        AUTH_ROUTE

    Log.d("package:mine", "Start destination is $startDestination")
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = ROOT_ROUTE,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ){
        authNavGraph(navController)
        composable(route = HOME_ROUTE){
            HomeScreen()
        }
    }
}