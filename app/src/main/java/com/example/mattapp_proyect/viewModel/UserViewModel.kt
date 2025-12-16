package com.example.mattapp_proyect.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Definir el estado de la UI para Autenticación
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

// 2. Usar ViewModel normal (no AndroidViewModel) ya que Supabase maneja la sesión
class UserViewModel : ViewModel() {

    private val repo = UserRepository()

    // --- Estados para la UI ---
    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser.asStateFlow()

    private val _files = MutableStateFlow<List<UploadedFile>>(emptyList())
    val files: StateFlow<List<UploadedFile>> = _files.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _historial = MutableStateFlow<List<HistorialItem>>(emptyList())
    val historial: StateFlow<List<HistorialItem>> = _historial.asStateFlow()

    // Estado para navegación de Login/Registro
    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            val userId = repo.getCurrentUserId()
            if (userId != null) {
                // Si hay sesión, intentamos cargar el perfil del usuario
                val user = repo.getCurrentUser()
                if (user != null) {
                    _loggedInUser.value = user
                    _authState.value = AuthUiState(isAuthenticated = true)
                    fetchUploadedFiles()
                }
            }
        }
    }


    fun loginUsuario(correo: String, pass: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null

            val success = repo.login(correo, pass)

            if (success) {
                val user = repo.getCurrentUser()
                if (user != null) {
                    _loggedInUser.value = user
                    _authState.value = AuthUiState(isAuthenticated = true)
                    fetchUploadedFiles()
                } else {
                    _errorMessage.value = "Error: Usuario no encontrado en base de datos"
                }
            } else {
                _errorMessage.value = "Error al iniciar sesión. Verifica credenciales."
            }
            _loading.value = false
        }
    }

    fun registraUsuario(nombre: String, correo: String, pass: String, rol: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null

            val newUser = User(nombre = nombre, correo = correo, rol = rol)

            val success = repo.register(newUser, pass)

            if (success) {
                val user = repo.getCurrentUser()
                if (user != null) {
                    _loggedInUser.value = user
                    _authState.value = AuthUiState(isAuthenticated = true)
                }
            } else {
                _errorMessage.value = "Error al registrar usuario."
            }
            _loading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _loggedInUser.value = null
            _files.value = emptyList()
            _historial.value = emptyList()
            _authState.value = AuthUiState(isAuthenticated = false)
        }
    }

    // Funciones de Datos

    fun fetchUploadedFiles() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Obtener archivos de Supabase
                _files.value = repo.getFiles()
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al cargar archivos"
            }
            _loading.value = false
        }
    }

    fun addUploadedFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            _loading.value = true
            val url = repo.uploadFile(context, uri)
            if (url != null) {
                fetchUploadedFiles()
            } else {
                _errorMessage.value = "Error al subir archivo"
            }
            _loading.value = false
        }
    }

    fun fetchHistorial() {
        val userId = _loggedInUser.value?.id ?: return
        viewModelScope.launch {
            _loading.value = true
            try {
                _historial.value = repo.getHistorial(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _loading.value = false
        }
    }

    // ... (dentro de la clase UserViewModel)

    // AGREGA ESTA FUNCIÓN NUEVA
    fun updateUserProfilePhoto(context: Context, uri: Uri) {
        viewModelScope.launch {
            _loading.value = true
            val currentUser = _loggedInUser.value

            if (currentUser?.id != null) {
                // 1. Subir la imagen al Storage (reutilizamos tu función existente)
                val url = repo.uploadFile(context, uri)

                if (url != null) {
                    // 2. IMPORTANTE: Guardar el link en la tabla de usuarios
                    repo.updateUserPhotoUrl(currentUser.id, url)

                    // 3. Actualizar la app inmediatamente para que no se borre la foto
                    _loggedInUser.value = currentUser.copy(fotoUri = url)
                } else {
                    _errorMessage.value = "Error al subir la imagen"
                }
            }
            _loading.value = false
        }
    }
}
