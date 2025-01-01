package com.example.socialconnect.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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
        ){  }
    }
}