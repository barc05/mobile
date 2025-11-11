package com.example.mattapp_proyect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.Preview
import com.example.mattapp_proyect.data.AppDatabase
import androidx.lifecycle.ViewModelProvider
import com.example.mattapp_proyect.ui.AppNavigation
import com.example.mattapp_proyect.ui.theme.MattApp_ProyectTheme
import com.example.mattapp_proyect.viewModel.UserViewModel
import com.example.mattapp_proyect.viewModel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dao = AppDatabase.getDatabase(application).userDao()

        val factory = UserViewModelFactory(dao)

        val viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            MattApp_ProyectTheme {
                // 4. Pasamos el ViewModel a nuestro sistema de navegación
                AppNavigation(userViewModel = viewModel)
            }
        }
    }
}
