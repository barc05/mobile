package com.example.mattapp_proyect.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado simplificado
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

class UserViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState

    init {
        val currentUserId = repo.getCurrentUserId()
        if (currentUserId != null) {
            _authState.value = AuthUiState(isAuthenticated = true)
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState(isLoading = true)
            val success = repo.login(email, pass)
            if (success) {
                _authState.value = AuthUiState(isAuthenticated = true)
            } else {
                _authState.value = AuthUiState(error = "Error al iniciar sesión")
            }
        }
    }

    fun register(nombre: String, email: String, pass: String, rol: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState(isLoading = true)
            val newUser = User(nombre = nombre, correo = email, rol = rol)
            val success = repo.register(newUser, pass)
            
            if (success) {
                _authState.value = AuthUiState(isAuthenticated = true)
            } else {
                _authState.value = AuthUiState(error = "Error al registrar")
            }
        }
    }
    
    fun uploadFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            _authState.value = AuthUiState(isLoading = true)
            val url = repo.uploadFile(context, uri)
            if (url != null) {
                println("Archivo subido: $url")
            } else {
                _authState.value = AuthUiState(error = "Error al subir archivo")
            }
            _authState.value = AuthUiState(isLoading = false, isAuthenticated = true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _authState.value = AuthUiState(isAuthenticated = false)
        }
    }
}
