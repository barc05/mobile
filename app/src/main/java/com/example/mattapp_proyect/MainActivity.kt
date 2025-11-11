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

import androidx.lifecycle.lifecycleScope
import com.example.mattapp_proyect.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dao = AppDatabase.getDatabase(application).userDao()

        lifecycleScope.launch(Dispatchers.IO) {
            // Define tus usuarios de prueba
            val user1 = User(
                nombre = "Sara",
                correo = "sara1@test.com",
                contraseña = "123456" // Ponles una contraseña
            )
            val user2 = User(
                nombre = "Luis",
                correo = "luis2@test.com",
                contraseña = "123456"
            )

            // Intenta insertarlos
            try {
                dao.insertUser(user1)
                dao.insertUser(user2)
            } catch (e: Exception) {

            }
        }

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
