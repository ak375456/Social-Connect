package com.example.socialconnect.authentication.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Biohazard
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.CircleUser
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Instagram
import com.composables.icons.lucide.Link
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Twitter
import com.composables.icons.lucide.Workflow
import com.example.socialconnect.navigation_setup.HOME_ROUTE
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTextField

@Composable
fun AdditionalInfoComposable(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var designation by remember{ mutableStateOf("") }
    var twitter by remember{ mutableStateOf("") }
    var website by remember{ mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.BottomEnd,
            ) {
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
                Icon(Lucide.Camera, "")
            }
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
            MyTextField(
                value = bio,
                onValueChanged = { bio = it },
                label = "Bio",
                painter = Lucide.Biohazard,
                isError = isError,
            )
            MyTextField(
                value = designation,
                onValueChanged = { designation = it },
                label = "Designation",
                painter = Lucide.Workflow,
                isError = isError,
            )
            MyTextField(
                value = twitter,
                onValueChanged = { twitter = it },
                label = "Instagram",
                painter = Lucide.Instagram,
            )
            MyTextField(
                value = website,
                onValueChanged = { website = it },
                label = "Website",
                painter = Lucide.Link,
            )
            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                MyButton(
                    onClick = {
                        if (firstName.isEmpty() || lastName.isEmpty() || number.isEmpty()) {
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
                            profilePictureLink = "https://i.ibb.co/jwZ2sch/IMG-20240826-060050-627.jpg",
                            bio = bio,
                            designation = designation,
                            twitter = twitter,
                            website = website
                        )
                    },
                    text = "Continue"
                )
            }
        }
    }
}