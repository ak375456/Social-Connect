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
                        SettingScreenContent(userData = userData!!)
                    } else {
                        Text(text = "No user data available")
                    }
                }

                else -> Unit
            }
        }
    }


@Composable
fun SettingScreenContent(userData: Map<String, Any>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var number by remember { mutableStateOf("") }
        var designation by remember { mutableStateOf("") }
        var twitter by remember { mutableStateOf("") }
        var website by remember { mutableStateOf("") }
        var id by remember { mutableStateOf("") }
        var bio by remember { mutableStateOf("") }


        Text(text = "Update your profile information here; the ID field is read-only.")

        MyTextField(
            value = userData["id"].toString(),
            onValueChanged = { id = it },
            label = "ID",
            painter = Lucide.IdCard,
            isReadOnly = true
        )
        MyTextField(
            value = userData["firstName"].toString(),
            onValueChanged = { firstName = it },
            label = "First Name",
            painter = Lucide.CircleUser,

        )
        MyTextField(
            value = userData["lastName"].toString(),
            onValueChanged = { lastName = it },
            label = "Last Name",
            painter = Lucide.CircleUser,
        )
        MyTextField(
            value = userData["email"].toString(),
            onValueChanged = { lastName = it },
            label = "Email",
            painter = Lucide.Mail,
        )
        MyTextField(
            value = userData["number"].toString(),
            onValueChanged = { number = it },
            label = "Number",
            painter = Lucide.Hash,
        )
        MyTextField(
            value = userData["bio"].toString(),
            onValueChanged = { bio = it },
            label = "Bio",
            painter = Lucide.Biohazard,
        )
        MyTextField(
            value = userData["designation"].toString(),
            onValueChanged = { designation = it },
            label = "Designation",
            painter = Lucide.Workflow,
        )
        MyTextField(
            value = userData["twitter"].toString(),
            onValueChanged = { twitter = it },
            label = "Instagram",
            painter = Lucide.Instagram,
        )
        MyTextField(
            value = userData["website"].toString(),
            onValueChanged = { website = it },
            label = "Website",
            painter = Lucide.Link,
        )
    }
}