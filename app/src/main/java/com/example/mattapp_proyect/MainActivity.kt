package com.example.mattapp_proyect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mattapp_proyect.ui.AppNavigation
import com.example.mattapp_proyect.ui.theme.MattApp_ProyectTheme
import com.example.mattapp_proyect.viewModel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MattApp_ProyectTheme {
                val viewModel: UserViewModel = viewModel()
                AppNavigation(userViewModel = viewModel)
            }
        }
    }
}