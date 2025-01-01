package com.example.socialconnect.home.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.socialconnect.home.BottomAppBarHolder
import com.example.socialconnect.navigation_setup.navigation_graphs.HomeNavGraph

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomAppBarHolder(navController) }
    ) {
        it.calculateTopPadding()
        HomeNavGraph(navController)
    }
}

