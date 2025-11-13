package com.example.mattapp_proyect.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile

/**
 * (IL2.3) Este es el ViewModel que maneja la lógica de
 * Registro e Inicio de Sesión (Autenticación).
 */
class UserViewModel() : ViewModel() {
    private val simulatedUsers = mutableListOf(
        User(
            nombre = "Mateo",
            correo = "mateo@test.com",
            contraseña = "123456",
            rol = "Maestro"
        ),
        User(
            nombre = "Juan",
            correo = "juan@test.com",
            contraseña = "123456",
            rol = "Alumno"
        )
    )

    private val simulatedFiles = mutableListOf(
        UploadedFile(
            id = 1,
            userEmail = "mateo@test.com", // Pertenece a Mateo (el Maestro)
            nombre = "Guía de Álgebra (Predefinida)",
            materia = "Matemáticas",
            // Esta es la clave: la URI es solo un texto. No apunta a ningún
            // archivo real, así evitamos usar res/raw.
            fileUri = "preloaded://guia_algebra_01"
        )
    )

    private val historialUsuario1 = listOf(
        HistorialItem(1, "Matemáticas", "Quiz", "2025-11-10", 85),
        HistorialItem(2, "Historia", "Quiz", "2025-11-08", 70)
    )

    // Datos para el Usuario 2
    private val historialUsuario2 = listOf(
        HistorialItem(3, "Ciencias", "Documento", "2025-11-09", 90),
        HistorialItem(4, "Matemáticas", "Documento", "2025-11-07", 100)
    )



    //usuario logueado
    val loggedInUser = mutableStateOf<User?>(null)

    // Función llamada desde la UI (RegisterScreen)
    fun registraUsuario(nombre: String, correo: String, contraseña: String, rol: String) {
        // 2. Pasa el 'role' al crear el usuario
        val newUser = User(nombre = nombre, correo = correo, contraseña = contraseña, rol = rol)
        simulatedUsers.add(newUser)
    }
    fun loginUsuario(correo: String, contraseña: String): User? {
        // Busca en la lista simulada
        val usuarioEncontrado = simulatedUsers.find {
            it.correo == correo && it.contraseña == contraseña
        }

        if (usuarioEncontrado != null) {
            loggedInUser.value = usuarioEncontrado // Login correcto
            return usuarioEncontrado
        } else {
            return null // Usuario o contraseña incorrectos
        }
    }

    // Actualizar foto usuario
    fun updateUser(user: User) {
        // En la simulación, solo necesitamos actualizar el estado 'en vivo'
        loggedInUser.value = user

        // (Opcional: actualizar la lista simulada)
        val index = simulatedUsers.indexOfFirst { it.correo == user.correo }
        if (index != -1) {
            simulatedUsers[index] = user
        }
    }

    // Cerrar sesion
    fun logout() {
        loggedInUser.value = null
    }

    // --- FUNCIÓN NUEVA SIMULADA ---

    /**
     * Devuelve una lista de historial FALSA basada
     * en el email del usuario que inició sesión.
     */
    fun getHistorialParaUsuario(): List<HistorialItem> {
        // obtener correo usuario logeado
        val emailUsuario = loggedInUser.value?.correo

        // Compara correo devueleve lista
        return when (emailUsuario) {
            "mateo@test.com" -> historialUsuario1
            "juan@test.com" -> historialUsuario2
            else -> emptyList() // Si es otro usuario, no muestra nada
        }
    }

    fun getUploadedFilesForUser(): List<UploadedFile> {
        // 1. Obtiene usuario y rol
        val currentUser = loggedInUser.value
        val userRol = currentUser?.rol

        // Si no hay usuario, lista vacía
        if (currentUser == null) {
            return emptyList()
        }

        // 2. Comprueba ROL usuario
        if (userRol == "Alumno") {
            // SI ALUMNO:
            // a. Encuentra  correos de todos los maestros
            val maestroEmails = simulatedUsers
                .filter { it.rol == "Maestro" }
                .map { it.correo }

            // b. Devuelve todos los archivos cuyo 'userEmail' esté en la lista de maestros
            return simulatedFiles.filter { it.userEmail in maestroEmails }

        } else {
            // SI ES MAESTRO (o cualquier otro rol):
            // Devuelve solo los archivos que le pertenecen (lógica original)
            return simulatedFiles.filter { it.userEmail == currentUser.correo }
        }
    }

    fun addUploadedFile(nombre: String, materia: String, uri: String) {
        val email = loggedInUser.value?.correo ?: return
        // Genera un ID nuevo (basado en el Proyecto 3)
        val newId = (simulatedFiles.maxByOrNull { it.id }?.id ?: 0) + 1

        simulatedFiles.add(
            UploadedFile(
                id = newId,
                userEmail = email,
                nombre = nombre,
                materia = materia,
                fileUri = uri
            )
        )
    }
}
