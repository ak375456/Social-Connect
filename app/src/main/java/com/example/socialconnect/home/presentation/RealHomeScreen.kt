package com.example.socialconnect.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialconnect.authentication.presentation.AuthState
import com.example.socialconnect.authentication.presentation.AuthViewModel
import com.example.socialconnect.home.BottomAppBarHolder
import com.example.socialconnect.navigation_setup.AUTH_ROUTE
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.example.socialconnect.navigation_setup.ROOT_ROUTE


@Composable
fun RealHomeScreen(navController: NavHostController) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()

    // Handle Logout State
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.popBackStack()
            navController.navigate(AUTH_ROUTE) {
                popUpTo(ROOT_ROUTE)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Home Screen")
        MyButton(
            text = "Logout",
            isEmptyBackground = false,
            onClick = {
                authViewModel.signOut()
            }
        )
    }
}


