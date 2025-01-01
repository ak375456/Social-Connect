package com.example.socialconnect.authentication.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.socialconnect.util.MyTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.CircleUser
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.example.socialconnect.navigation_setup.HOME_ROUTE
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTextField

@Composable
fun AdditionalInfoComposable(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.popBackStack()
                navController.navigate(HOME_ROUTE)
            }

            is AuthState.Error -> {
                val error = (authState as AuthState.Error).message
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
                MyTopAppBar("Additional Information")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {


                MyTextField(
                    value = firstName,
                    onValueChanged = { firstName = it },
                    label = "First Name",
                    painter = Lucide.CircleUser,
                    isError = isError,
                    supportingTextFunction = {
                        if (isError) Text(errorMessage)
                    }
                )
                MyTextField(
                    value = lastName,
                    onValueChanged = { lastName = it },
                    label = "Last Name",
                    painter = Lucide.CircleUser,
                    isError = isError,
                    supportingTextFunction = {
                        if (isError) Text(errorMessage)
                    }
                )
                MyTextField(
                    value = number,
                    onValueChanged = { number = it },
                    label = "Number",
                    painter = Lucide.Hash,
                    isError = isError,
                    supportingTextFunction = {
                        if (isError) Text(errorMessage)
                    }
                )
                if(authState is AuthState.Loading){
                    CircularProgressIndicator()
                }else{
                    MyButton(
                        onClick = {
                            if (firstName.isEmpty() || lastName.isEmpty()|| number.isEmpty()) {
                                isError = true
                                errorMessage = "Please fill all the fields"
                                return@MyButton
                            }
                            isError = false
                            errorMessage = ""
                            authViewModel.createUser(
                                firstName = firstName,
                                lastName = lastName,
                                number = number,
                                profilePictureLink = "TODO()"
                            )
                        },
                        text = "Continue"
                    )
                }
            }
        }
    }