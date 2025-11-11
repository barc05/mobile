package com.example.mattapp_proyect.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.data.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mattapp_proyect.data.model.HistorialItem

/**
 * (IL2.3) Este es el ViewModel que maneja la lógica de
 * Registro e Inicio de Sesión (Autenticación).
 */
class UserViewModel(private val dao: UserDao) : ViewModel() {

    //usuario logueado
    val loggedInUser = mutableStateOf<User?>(null)

    // Función llamada desde la UI (RegisterScreen)
    fun registraUsuario(nombre: String, correo: String, contraseña: String) {
        // (IL2.2) Inicia una corutina en un hilo de background (IO)
        // para no bloquear la UI mientras se escribe en la BD.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newUser = User(nombre = nombre, correo = correo, contraseña = contraseña)
                dao.insertUser(newUser)
                // (Aquí podríamos navegar a Home o mostrar un mensaje de éxito)
            } catch (e: Exception) {
                // (Manejar error, ej: email ya existe)
            }
        }
    }
    suspend fun loginUsuario(correo: String, contraseña: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val usuarioEncontrado = dao.getUserByCorreo(correo)
                if (usuarioEncontrado == null) {
                    null // Usuario no existe
                } else {
                    if (usuarioEncontrado.contraseña == contraseña) {
                        usuarioEncontrado // Login correcto
                    } else {
                        null // Contraseña incorrecta
                    }
                }
            } catch (e: Exception) {
                null // Error
            }
        }
    }

    // Actualizar foto usuario
    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateUser(user)
            // Actualizar el estado local
            loggedInUser.value = user
        }
    }

    // Cerrar sesion
    fun logout() {
        loggedInUser.value = null
    }

    // --- DATOS SIMULADOS  ---

    // Datos para el Usuario 1
    private val historialUsuario1 = listOf(
        HistorialItem(1, "Matemáticas", "Quiz", "2025-11-10", 85),
        HistorialItem(2, "Historia", "Quiz", "2025-11-08", 70)
    )

    // Datos para el Usuario 2
    private val historialUsuario2 = listOf(
        HistorialItem(3, "Ciencias", "Documento", "2025-11-09", 90),
        HistorialItem(4, "Matemáticas", "Documento", "2025-11-07", 100)
    )

    // --- FUNCIÓN NUEVA SIMULADA ---

    /**
     * Devuelve una lista de historial FALSA basada
     * en el email del usuario que inició sesión.
     */
    fun getHistorialParaUsuario(): List<HistorialItem> {
        // Obtiene el email del usuario logueado
        val emailUsuario = loggedInUser.value?.correo

        // Compara el email y devuelve la lista correspondiente
        return when (emailUsuario) {
            "usuario1@test.com" -> historialUsuario1
            "usuario2@test.com" -> historialUsuario2
            else -> emptyList() // Si es otro usuario, no muestra nada
        }
    }
}


    // (Añadiremos la función de login aquí más adelante)

/**
 * (IL2.3) Factory para crear el AuthViewModel.
 * Esto es necesario porque nuestro ViewModel tiene un parámetro
 * en el constructor (el 'dao').
 */
class UserViewModelFactory(private val dao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


/**
 * (IL2.3) Verifica si un usuario existe en la base de datos.
 * Esta es una función de suspensión, por lo que debe ser llamada desde una corutina.
 */
