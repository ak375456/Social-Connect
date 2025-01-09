package com.example.socialconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.socialconnect.navigation_setup.SetupNavGraph
import com.example.socialconnect.ui.theme.SocialConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // this graph will decide which composable to show,
            //if logged in then home screen otherwise login screen
            SocialConnectTheme {
                SetupNavGraph(navController = rememberNavController())
            }
        }
    }
}


