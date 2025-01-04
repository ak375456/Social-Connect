package com.example.socialconnect.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.Biohazard
import com.composables.icons.lucide.CircleUser
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.IdCard
import com.composables.icons.lucide.Instagram
import com.composables.icons.lucide.Link
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.Workflow
import com.example.socialconnect.authentication.presentation.AuthState
import com.example.socialconnect.authentication.presentation.AuthViewModel
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTextField
import com.example.socialconnect.util.MyTopAppBar

@Composable
fun SettingScreen(authViewModel: AuthViewModel = hiltViewModel()) {

    val authState by authViewModel.authState.collectAsState()
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getUserData()
    }
        Box(
            modifier = Modifier

                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (authState) {
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthState.Error -> {
                    val errorMessage = (authState as AuthState.Error).message
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }

                is AuthState.Success -> {
                    if (userData != null) {
                        SettingScreenContent(userData = userData!!,authViewModel = authViewModel)
                    } else {
                        Text(text = "No user data available")
                    }
                }

                else -> Unit
            }
        }
    }


@Composable
fun SettingScreenContent(
    userData: Map<String, Any>,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    // Initializing state variables with Firestore values
    var firstName by remember { mutableStateOf(userData["firstName"].toString()) }
    var lastName by remember { mutableStateOf(userData["lastName"].toString()) }
    var number by remember { mutableStateOf(userData["number"].toString()) }
    var bio by remember { mutableStateOf(userData["bio"].toString()) }
    var designation by remember { mutableStateOf(userData["designation"].toString()) }
    var twitter by remember { mutableStateOf(userData["twitter"].toString()) }
    var website by remember { mutableStateOf(userData["website"].toString()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(text = "Update your profile information here; the ID field is read-only.")

        MyTextField(
            value = userData["id"].toString(),
            onValueChanged = { /* ID is read-only */ },
            label = "ID",
            painter = Lucide.IdCard,
            isReadOnly = true
        )
        MyTextField(
            value = firstName,
            onValueChanged = { firstName = it },
            label = "First Name",
            painter = Lucide.CircleUser
        )
        MyTextField(
            value = lastName,
            onValueChanged = { lastName = it },
            label = "Last Name",
            painter = Lucide.CircleUser
        )
        MyTextField(
            value = number,
            onValueChanged = { number = it },
            label = "Number",
            painter = Lucide.Hash
        )
        MyTextField(
            value = bio,
            onValueChanged = { bio = it },
            label = "Bio",
            painter = Lucide.Biohazard
        )
        MyTextField(
            value = designation,
            onValueChanged = { designation = it },
            label = "Designation",
            painter = Lucide.Workflow
        )
        MyTextField(
            value = twitter,
            onValueChanged = { twitter = it },
            label = "Twitter",
            painter = Lucide.Instagram
        )
        MyTextField(
            value = website,
            onValueChanged = { website = it },
            label = "Website",
            painter = Lucide.Link
        )
        MyButton(
            text = "Update Profile",
            onClick = {
                // Create a map of only updated values
                val updates = mutableMapOf<String, Any>()
                if (firstName != userData["firstName"].toString()) updates["firstName"] = firstName
                if (lastName != userData["lastName"].toString()) updates["lastName"] = lastName
                if (number != userData["number"].toString()) updates["number"] = number
                if (bio != userData["bio"].toString()) updates["bio"] = bio
                if (designation != userData["designation"].toString()) updates["designation"] = designation
                if (twitter != userData["twitter"].toString()) updates["twitter"] = twitter
                if (website != userData["website"].toString()) updates["website"] = website

                // Call the ViewModel function to update Firestore
                if (updates.isNotEmpty()) {
                    authViewModel.updateUserProfile(updates)
                }
            },
            isEmptyBackground = true
        )
    }
}