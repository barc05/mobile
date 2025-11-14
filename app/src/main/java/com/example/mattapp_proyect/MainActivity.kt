package com.example.mattapp_proyect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.mattapp_proyect.ui.AppNavigation
import com.example.mattapp_proyect.ui.theme.MattApp_ProyectTheme
import com.example.mattapp_proyect.viewModel.UserViewModel
import androidx.lifecycle.lifecycleScope
import com.example.mattapp_proyect.data.AppDataBase
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.viewModel.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicializa la Base de Datos y los DAOs
        val database = AppDataBase.getDatabase(application)
        val userDao = database.userDao()
        val historialDao = database.historialDao()
        val fileDao = database.uploadedFileDao()

        // 2. PRECARGA los datos simulados en la BD (solo la primera vez)
        lifecycleScope.launch(Dispatchers.IO) {
            // Precarga Usuarios
            val user1 = User("Mateo", "mateo@test.com", "123456", "Maestro")
            val user2 = User("Juan", "juan@test.com", "123456", "Alumno")
            userDao.insertUser(user1)
            userDao.insertUser(user2)

            // Precarga Historial
            historialDao.insertHistorial(HistorialItem(userEmail = "mateo@test.com", materia = "Matemáticas", tipoArchivo = "Quiz", fecha = "2025-11-10"))
            historialDao.insertHistorial(HistorialItem(userEmail = "mateo@test.com", materia = "Historia", tipoArchivo = "Quiz", fecha = "2025-11-08"))
            historialDao.insertHistorial(HistorialItem(userEmail = "juan@test.com", materia = "Ciencias", tipoArchivo = "Documento", fecha = "2025-11-09"))

            // --- Precarga Archivos (MODIFICADA) ---
            fileDao.insertFile(UploadedFile(userEmail = "mateo@test.com", nombre = "Guía de Álgebra (Predefinida)", materia = "Matemáticas"))
        }

        // 3. Crea el Factory y pásale los 3 DAOs
        val factory = UserViewModelFactory(userDao, historialDao, fileDao)

        // 4. Obtén el ViewModel usando el Factory
        val viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        setContent {
            MattApp_ProyectTheme {
                AppNavigation(userViewModel = viewModel)
            }
        }
    }
}
