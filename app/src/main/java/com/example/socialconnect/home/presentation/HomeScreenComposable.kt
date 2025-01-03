package com.example.socialconnect.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.socialconnect.home.BottomAppBarHolder
import com.example.socialconnect.navigation_setup.navigation_graphs.HomeNavGraph
import androidx.compose.runtime.getValue
import com.example.socialconnect.navigation_setup.BottomAppBarScreen
import com.example.socialconnect.util.MyTopAppBar

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = { DynamicAppBar(currentRoute, navController) },
        bottomBar = { BottomAppBarHolder(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeNavGraph(navController)
        }
    }
}

@Composable
fun DynamicAppBar(currentRoute: String?, navController: NavHostController) {
    when (currentRoute) {
        BottomAppBarScreen.HomeScreen.route -> {
            MyTopAppBar(title = "Home")
        }
        BottomAppBarScreen.ProfileScreen.route -> {
            MyTopAppBar(
                title = "Profile",
                onNavigationClick  = { navController.popBackStack() }
            )
        }
        BottomAppBarScreen.SettingScreen.route -> {
            MyTopAppBar(title = "Settings")
        }
        else -> {
            MyTopAppBar(title = "SocialConnect")
        }
    }
}



