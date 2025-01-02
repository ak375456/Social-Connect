package com.example.socialconnect.authentication.presentation

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.ArrowBigLeft
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.Lucide
import com.example.socialconnect.navigation_setup.HOME_ROUTE
import com.example.socialconnect.navigation_setup.Screens
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTextField
import com.example.socialconnect.util.MyTopAppBar

@Composable
fun SignupComposable(navController: NavHostController) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.popBackStack()
                navController.navigate(HOME_ROUTE)
            }
            is AuthState.Error -> {
                val error = (authState as AuthState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                authViewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                "Signup",
                navigationIcon = Lucide.ArrowBigLeft,
                onNavigationClick = {
                    navController.popBackStack()
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Hello! Register to get Started",
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Box (
                modifier = Modifier.size(80.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.BottomEnd,
            ){
                if (imageUri != null) {
                    // Show selected image using Coil
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    )
                }
                Icon(Lucide.Camera,"")
            }
            MyTextField(
                value = email,
                onValueChanged = { email = it },
                label = "Email",
                painter = Icons.Default.Email,
                isError = isError,
                supportingTextFunction = { Text(text = errorMessage) }
            )

            MyTextField(
                value = password,
                onValueChanged = { password = it },
                label = "Password",
                painter = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = authViewModel.isPasswordVisible,
                onPasswordToggle = authViewModel::togglePasswordVisibility,
                isError = isError,
                supportingTextFunction = { Text(text = errorMessage) }
            )

            MyTextField(
                value = confirmPassword,
                onValueChanged = { confirmPassword = it },
                label = "Confirm Password",
                painter = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = authViewModel.isPasswordVisible,
                onPasswordToggle = authViewModel::togglePasswordVisibility,
                isError = isError,
                supportingTextFunction = { Text(text = errorMessage) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                MyButton(
                    "Register",
                    onClick = {
                        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            isError = true
                            errorMessage = "Please fill all fields"
                            return@MyButton
                        }
                        if (password != confirmPassword) {
                            isError = true
                            errorMessage = "Passwords do not match"
                            return@MyButton
                        }
                        isError = false
                        errorMessage = ""
                        navController.navigate(route = Screens.AdditionalInformationScreen.route)
                        authViewModel.createUserWithEmailAndPassword(email.toString(), password.toString())
                    },
                    isEmptyBackground = false
                )
            }

            MyButton(
                "Already Have an Account? Login",
                onClick = { navController.popBackStack() },
                isEmptyBackground = true
            )
        }
    }
}
