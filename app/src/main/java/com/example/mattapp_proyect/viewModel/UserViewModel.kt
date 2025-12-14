package com.example.mattapp_proyect.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.repository.UserRepository
import com.example.mattapp_proyect.data.model.LoginRequest
import com.example.mattapp_proyect.data.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UserRepository()

    private val sessionManager = SessionManager(application.applicationContext)

    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    private val _userToken = MutableStateFlow<String?>(null)
    val userToken: StateFlow<String?> = _userToken.asStateFlow()

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

    init {
        viewModelScope.launch {
            sessionManager.authToken.collect { token ->
                _userToken.value = token
            }
        }
        viewModelScope.launch {
            sessionManager.user.collect { savedUser ->
                _loggedInUser.value = savedUser
                if (savedUser != null) {
                    _authState.value = AuthUiState(isAuthenticated = true, user = savedUser)
                    fetchUploadedFiles()
                }
            }
        }
    }


    fun loginUsuario(correo: String, contraseña: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState(isLoading = true)
            val request = LoginRequest(correo, contraseña)
            try {
                val result = repo.loginUser(request)
                if (result != null && result.usuario != null && result.token != null) {
                    // Éxito
                    _authState.value = AuthUiState(isAuthenticated = true, user = result.usuario)
                    saveSessionData(result.usuario, result.token)
                    fetchUploadedFiles()
                } else {
                    _authState.value = AuthUiState(error = result?.mensaje ?: "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = AuthUiState(error = "Error de conexión: ${e.message}")
            } finally {
               _loading.value = false
            }
        }
    }

    fun registraUsuario(nombre: String, correo: String, contraseña: String, rol: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState(isLoading = true)
            val request = RegisterRequest(correo, contraseña, nombre, rol)
            try {
                val result = repo.registerUser(request)
                if (result != null && result.usuario != null && result.token != null) {
                    _authState.value = AuthUiState(isAuthenticated = true, user = result.usuario)
                    saveSessionData(result.usuario, result.token)
                    fetchUploadedFiles()
                } else {
                    _authState.value = AuthUiState(error = result?.mensaje ?: "Error al registrar")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = AuthUiState(error = "Error de conexión: ${e.message}")
            }
        }
    }

    private fun saveSessionData(user: User, token: String) {
        _loggedInUser.value = user
        _userToken.value = token
        
        viewModelScope.launch {
            sessionManager.saveSession(token, user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession() // Borra del disco
            _loggedInUser.value = null
            _userToken.value = null
            _files.value = emptyList()
            _authState.value = AuthUiState()
        }
    }

    // Funciones con token
    fun fetchUploadedFiles() {
        val currentUser = _loggedInUser.value ?: return
        val token = _userToken.value ?: return

        viewModelScope.launch {
            _loading.value = true
            try {
                _files.value = repo.getFilesForUser(currentUser.id, currentUser.rol, token)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al cargar archivos"
            } finally {
                _loading.value = false
            }
        }
    }

    fun addUploadedFile(context: Context, uri: Uri) {
        val currentUser = _loggedInUser.value ?: return
        val userId = currentUser.id ?: return
        val token = _userToken.value

        if (token == null) {
            _errorMessage.value = "Sesión no válida"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                repo.uploadFile(context, userId, uri, token)
                fetchUploadedFiles()
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al subir: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchHistorial() {
        val currentUser = _loggedInUser.value ?: return
        val userId = currentUser.id ?: return
        val token = _userToken.value ?: return

        viewModelScope.launch {
            _loading.value = true
            try {
                _historial.value = repo.getHistorial(userId, token)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}
