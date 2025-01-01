package com.example.socialconnect.navigation_setup.navigation_graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.socialconnect.authentication.presentation.ForgetPasswordComposable
import com.example.socialconnect.authentication.presentation.LoginComposable
import com.example.socialconnect.authentication.presentation.SignupComposable
import com.example.socialconnect.navigation_setup.AUTH_ROUTE
import com.example.socialconnect.navigation_setup.Screens

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    navigation(
        startDestination = Screens.LoginScreen.route,
        route = AUTH_ROUTE,
    ){
        composable(
            route = Screens.LoginScreen.route,
        ){
            LoginComposable(navController)
        }
        composable(
            route = Screens.SignupScreen.route,
        ){
            SignupComposable(navController)
        }
        composable(
            route = Screens.ForgetPasswordScreen.route,
        ){
            ForgetPasswordComposable(navController)
        }
    }
}