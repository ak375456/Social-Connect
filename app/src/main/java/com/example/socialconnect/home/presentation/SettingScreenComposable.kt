package com.example.socialconnect.home.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Biohazard
import com.composables.icons.lucide.CircleUser
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.IdCard
import com.composables.icons.lucide.Lucide
import com.example.socialconnect.util.MyTextField
import com.example.socialconnect.util.MyTopAppBar

@Composable
fun SettingScreen(){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(title = "Setting")
        }
    ) {innerPadding->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            var firstName by remember{ mutableStateOf("") }
            var lastName by remember{ mutableStateOf("") }
            var number by remember{ mutableStateOf("") }
            var id by remember{ mutableStateOf("") }
            var bio by remember{ mutableStateOf("") }
            var imageUri by remember{ mutableStateOf<Uri?>(null) }
            MyTextField(
                value = id,
                onValueChanged = {id=it},
                label = "ID",
                painter = Lucide.IdCard,
            )
            MyTextField(
                value = firstName,
                onValueChanged = {firstName=it},
                label = "First Name",
                painter = Lucide.CircleUser,
            )
            MyTextField(
                value = lastName,
                onValueChanged = {lastName=it},
                label = "Last Name",
                painter = Lucide.CircleUser,
            )
            MyTextField(
                value = number,
                onValueChanged = {number=it},
                label = "Number",
                painter = Lucide.Hash,
            )
            MyTextField(
                value = bio,
                onValueChanged = {bio=it},
                label = "Bio",
                painter = Lucide.Biohazard,
            )
        }
    }
}