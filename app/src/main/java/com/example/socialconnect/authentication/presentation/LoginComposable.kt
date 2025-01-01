package com.example.socialconnect.authentication.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialconnect.util.MyTopAppBar
import com.example.socialconnect.R
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.socialconnect.navigation_setup.HOME_ROUTE
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTextField
import com.example.socialconnect.navigation_setup.Screens

@Composable
fun LoginComposable(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    // Handle Authentication State
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.popBackStack()
                navController.navigate(HOME_ROUTE)
            }
            is AuthState.Error -> {
                val error = (authState as AuthState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                errorMessage = error
                isError = true
                authViewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                "Login",
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
            ) {
                Image(
                    painter = painterResource(id = if (!isSystemInDarkTheme()) R.drawable.light else R.drawable.dark),
                    contentDescription = "Logo"
                )
            }
            MyTextField(
                value = email,
                onValueChanged = { value ->
                    email = value
                },
                label = "Email",
                painter = Icons.Default.Email,
                isError = isError,
                supportingTextFunction = {
                    if (isError) Text(text = errorMessage)
                }
            )
            MyTextField(
                value = password,
                onValueChanged = { value ->
                    password = value
                },
                label = "Password",
                painter = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = authViewModel.isPasswordVisible,
                onPasswordToggle = authViewModel::togglePasswordVisibility,
                isError = isError,
                supportingTextFunction = {
                    if (isError) Text(text = errorMessage)
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    navController.navigate(route = Screens.ForgetPasswordScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }) {
                    Text("Forget Password?")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                MyButton(
                    "Login",
                    isEmptyBackground = false,
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            isError = true
                            errorMessage = "Please fill all the fields"
                            return@MyButton
                        }
                        isError = false
                        errorMessage = ""
                        authViewModel.signInUserWithEmailAndPassword(email, password)
                    }
                )
            }
            MyButton(
                "Register",
                onClick = {
                    navController.navigate(route = Screens.SignupScreen.route)
                },
                isEmptyBackground = true
            )
        }
    }
}
