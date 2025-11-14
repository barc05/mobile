package com.example.mattapp_proyect.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.dao.HistorialDao
import com.example.mattapp_proyect.data.dao.UploadedFileDao
import com.example.mattapp_proyect.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mattapp_proyect.data.dao.UserDao
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.text.SimpleDateFormat
import java.util.Date

/**
 * (IL2.3) Este es el ViewModel que maneja la lógica de
 * Registro e Inicio de Sesión (Autenticación).
 */
class UserViewModel(
    private val userDao: UserDao,
    private val historialDao: HistorialDao,
    private val fileDao: UploadedFileDao
) : ViewModel() {

    //usuario logueado
    val loggedInUser = mutableStateOf<User?>(null)

    // Función llamada desde la UI (RegisterScreen)
    fun registraUsuario(nombre: String, correo: String, contraseña: String, rol: String) { // <-- AÑADE 'rol'
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Pasa el 'rol' al constructor
                val newUser = User(nombre, correo, contraseña, rol) // <-- AÑADE 'rol'
                userDao.insertUser(newUser)
            } catch (e: Exception) {
                // (Manejar error, ej: email ya existe)
            }
        }
    }

    // --- LÓGICA DE LOGIN (USA LA BD) ---
    suspend fun loginUsuario(correo: String, contraseña: String): User? {
        return withContext(Dispatchers.IO) {
            val usuarioEncontrado = userDao.getUserByCorreo(correo)
            if (usuarioEncontrado != null && usuarioEncontrado.contraseña == contraseña) {
                withContext(Dispatchers.Main) {
                    loggedInUser.value = usuarioEncontrado
                }
                usuarioEncontrado
            } else {
                null
            }
        }
    }

    // Actualizar foto usuario
    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.updateUser(user)
            withContext(Dispatchers.Main) {
                loggedInUser.value = user
            }
        }
    }

    fun logout() {
        loggedInUser.value = null
    }

    // --- LÓGICA DE HISTORIAL (USA LA BD) ---
    fun getHistorialParaUsuario(): Flow<List<HistorialItem>> {
        val emailUsuario = loggedInUser.value?.correo
        return if (emailUsuario != null) {
            historialDao.getHistorialForUser(emailUsuario)
        } else {
            emptyFlow() // Devuelve un flujo vacío si no hay usuario
        }
    }

    fun addHistorialItem(materia: String, tipo: String, puntuacion: Int) {
        val email = loggedInUser.value?.correo ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val fechaActual = sdf.format(Date())

            val nuevoItem = HistorialItem(
                userEmail = email,
                materia = materia,
                tipoArchivo = tipo,
                fecha = fechaActual,
            )
            historialDao.insertHistorial(nuevoItem)
        }
    }

    // --- LÓGICA DE ARCHIVOS (USA LA BD) ---
    fun getUploadedFilesForUser(): Flow<List<UploadedFile>> {
        val currentUser = loggedInUser.value
        if (currentUser == null) return emptyFlow()

        return if (currentUser.rol == "Alumno") {
            // Si es Alumno, trae los archivos del rol "Maestro"
            fileDao.getFilesForRole("Maestro")
        } else {
            // Si es Maestro, trae solo sus propios archivos
            fileDao.getFilesForUser(currentUser.correo)
        }
    }

    fun addUploadedFile(nombre: String, materia: String, uri: String) {
        val email = loggedInUser.value?.correo ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val newFile = UploadedFile(
                userEmail = email,
                nombre = nombre,
                materia = materia
            )
            fileDao.insertFile(newFile)
        }
    }
}
class UserViewModelFactory(
    private val userDao: UserDao,
    private val historialDao: HistorialDao,
    private val fileDao: UploadedFileDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao, historialDao, fileDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}