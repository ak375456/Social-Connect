package com.example.socialconnect.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.socialconnect.navigation_setup.BottomAppBarScreen

@Composable
fun BottomAppBarHolder(navController: NavHostController) {



    val items = listOf(
        BottomAppBarScreen.HomeScreen,
        BottomAppBarScreen.ProfileScreen,
        BottomAppBarScreen.SettingScreen,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = items.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            items.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,

                )
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun RowScope.AddItem(
    screen: BottomAppBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val interactionSource = remember { MutableInteractionSource() }
    NavigationBarItem(
        label = { Text(text = screen.label) },
        icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = LocalContentColor.current.copy(alpha = 0.3f),
            unselectedTextColor = LocalContentColor.current.copy(alpha = 0.3f),
        ),
        interactionSource = interactionSource,
        onClick = {
            navController.navigate(screen.route){
                Log.d("NAVIGATION",navController.graph.findStartDestination().displayName.toString())
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
    )
}
