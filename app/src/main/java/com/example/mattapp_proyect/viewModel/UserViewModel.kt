package com.example.mattapp_proyect.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repo = UserRepository()

    // --- Estado de Usuario Logueado ---
    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser.asStateFlow()

    // --- Lista de Archivos ---
    private val _files = MutableStateFlow<List<UploadedFile>>(emptyList())
    val files: StateFlow<List<UploadedFile>> = _files.asStateFlow()

    // --- Estados de carga y error (como en el ejemplo) ---
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // --- FUNCIONES ---

    fun loginUsuario(correo: String, contraseña: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                // Obtenemos todos los usuarios y filtramos (simulación de login)
                val users = repo.getUsers()
                val user = users.find { it.correo == correo && it.contraseña == contraseña }

                if (user != null) {
                    _loggedInUser.value = user
                    fetchUploadedFiles() // Cargar archivos al loguear
                } else {
                    _errorMessage.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun registraUsuario(nombre: String, correo: String, contraseña: String, rol: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val newUser = User(
                    id = null, // El servidor genera el UUID
                    nombre = nombre,
                    correo = correo,
                    contraseña = contraseña,
                    rol = rol
                )
                repo.createUser(newUser)
                // Opcional: Auto-login
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al registrar"
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchUploadedFiles() {
        val currentUser = _loggedInUser.value ?: return
        viewModelScope.launch {
            _loading.value = true
            try {
                _files.value = repo.getFilesForUser(currentUser.id, currentUser.rol)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    // Esta función usa el nuevo MultipartUtils igual que el ejemplo uploadUserWithImage
    fun addUploadedFile(context: Context, uri: Uri) {
        val currentUser = _loggedInUser.value ?: return
        val userId = currentUser.id ?: return

        viewModelScope.launch {
            _loading.value = true
            try {
                repo.uploadFile(context, userId, uri)
                fetchUploadedFiles() // Recargar lista
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al subir archivo"
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
        _files.value = emptyList()
    }
}