package com.example.socialconnect.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.socialconnect.home.BottomAppBarHolder
import com.example.socialconnect.navigation_setup.navigation_graphs.HomeNavGraph
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.example.socialconnect.authentication.presentation.AuthViewModel
import com.example.socialconnect.navigation_setup.BottomAppBarScreen
import com.example.socialconnect.navigation_setup.Screens
import com.example.socialconnect.util.MyTopAppBar

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()

    val currentUserId = authViewModel.getCurrentUserId().orEmpty()

    Scaffold(
        topBar = { DynamicAppBar(currentRoute, navController) },
        bottomBar = { BottomAppBarHolder(navController) },
        floatingActionButton = {
            if (currentRoute == BottomAppBarScreen.HomeScreen.route) {
                FloatingActionButton(onClick = {
                    navController.navigate(route = Screens.PostScreen.route)
                }) {
                    Icon(Lucide.Plus,"")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeNavGraph(
                navController,

            )
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
            MyTopAppBar(title = "Chat")
        }
    }
}



