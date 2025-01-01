package com.example.socialconnect.authentication.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.ArrowBigLeft
import com.composables.icons.lucide.Lucide
import com.example.socialconnect.util.MyButton
import com.example.socialconnect.util.MyTextField
import com.example.socialconnect.util.MyTopAppBar

@Composable
fun ForgetPasswordComposable(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                "Forget",
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
                .fillMaxSize()
        ) {
            Text(
                text = "Oops! We got you!",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(
                modifier = Modifier.height(18.dp)
            )
            MyTextField(
                value = email,
                onValueChanged = { value ->
                    email = value
                },
                label = "Email",
                painter = Icons.Default.Email,
            )
            MyButton(
                "Send Code!",
                onClick = { },
                isEmptyBackground = false
            )
            Spacer(
                modifier = Modifier.height(28.dp)
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "We will send you a code to reset your password. Please be patient the code can take up to two minutes",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}