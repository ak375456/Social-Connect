package com.example.socialconnect.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.socialconnect.util.MyTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.socialconnect.authentication.presentation.AuthState
import com.example.socialconnect.authentication.presentation.AuthViewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.composables.icons.lucide.Earth
import com.composables.icons.lucide.Instagram
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.Phone
import com.composables.icons.lucide.PhoneCall
import com.example.socialconnect.ui.theme.*

@Composable
fun ProfileScreen(authViewModel: AuthViewModel = hiltViewModel()){

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
                        UserProfileContent(userData = userData!!)
                    } else {
                        Text(text = "No user data available")
                    }
                }
                else -> Unit
            }
        }
    }


@Composable
fun UserProfileContent(userData: Map<String, Any>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        userData["profilePictureLink"]?.let { profilePictureLink ->
            Image(
                painter = rememberAsyncImagePainter(profilePictureLink),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text ="${userData["firstName"]} ${userData["lastName"]}",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
        Text(
            text = "${userData["designation"] ?: "N/A"}",
            style = TextStyle(
                fontWeight = FontWeight.ExtraLight,
                fontSize = 16.sp
            )
        )
        ExpandableTextWithEllipsis(text = userData["bio"] as? String ?: "N/A")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(onClick = {
            }) {
                Icon(Lucide.PhoneCall, "")
                Spacer(Modifier.width(6.dp))
                Text("Call")
            }
            Button(onClick = {
            }) {
                Icon(Lucide.Mail, "")
                Spacer(Modifier.width(6.dp))
                Text("Email")
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding(start = 18.dp),
        ){
            Icon(Lucide.Earth,"", tint = Color.Cyan)
            Spacer(Modifier.width(24.dp))
            Text(
                text = "${userData["website"] ?: "N/A"}",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding(start = 18.dp),
        ){
            Icon(Lucide.Instagram,"", tint = Color.Magenta)
            Spacer(Modifier.width(24.dp))
            Text(
                text = "${userData["twitter"] ?: "N/A"}",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding(start = 18.dp),
        ){
            Icon(Lucide.Phone,"", tint = Color.Green)
            Spacer(Modifier.width(24.dp))
            Text(
                text = "${userData["number"] ?: "N/A"}",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding(start = 18.dp),
        ){
            Icon(Lucide.Mail,"", tint = blueMail)
            Spacer(Modifier.width(24.dp))
            Text(
                text = "${userData["email"] ?: "N/A"}",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(text = "Email: ${userData["email"] ?: "N/A"}")
    }
}

@Composable
fun ExpandableTextWithEllipsis(
    text: String,
    collapsedMaxLines: Int = 2
) {
    var isExpanded by remember { mutableStateOf(false) }
    val textColor = MaterialTheme.colorScheme.primary

    Box(modifier = Modifier.fillMaxWidth()) {
        if (isExpanded) {
            // Expanded Text
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            // Collapsed Text with ellipsis
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = text,
                    maxLines = collapsedMaxLines,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f) // Take remaining space
                )
                Text(
                    text = "See More",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { isExpanded = true }
                )
            }
        }
    }
}
