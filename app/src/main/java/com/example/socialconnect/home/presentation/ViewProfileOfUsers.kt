package com.example.socialconnect.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.socialconnect.post_feature.presentation.PostViewModel
import com.example.socialconnect.post_feature.presentation.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Earth
import com.composables.icons.lucide.Instagram
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.Phone
import com.composables.icons.lucide.PhoneCall
import com.example.socialconnect.ui.theme.blueMail

//Fix Ui tomorrow for profile Screen (FIXED)

@Composable
fun ViewProfile(userId: String, postViewModel: PostViewModel = hiltViewModel()) {
    // this User is not from Firebase Authentication, it is a repository.
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(userId) {
        user = postViewModel.getUserById(userId)
    }

    if (user != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            user!!.profilePictureLink.let { profilePictureLink ->
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
            Text(text = "${user!!.firstName} ${user!!.lastName}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )
            Text(
                text = user!!.designation,
                style = TextStyle(
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 16.sp
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${user!!.followers.size}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "Followers",
                        style = TextStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${user!!.following.size}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "Following",
                        style = TextStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    )
                }
            }
            ExpandableTextWithEllipsis(text = user!!.bio)

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
                    text = user!!.website,
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
                    text = user!!.twitter,
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
                    text = user!!.number,
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
                    text = user!!.email,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    } else {
        Text("Loading profile...")
    }
}

