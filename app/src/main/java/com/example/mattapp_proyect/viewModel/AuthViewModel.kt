package com.example.mattapp_proyect.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mattapp_proyect.data.model.AuthResponse
import com.example.mattapp_proyect.data.model.LoginRequest
import com.example.mattapp_proyect.data.model.RegisterRequest
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class AuthViewModel : ViewModel() {

    private val repository = UserRepository()
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _userToken = MutableStateFlow<String?>(null)
    val userToken: StateFlow<String?> = _userToken

    // Función para Iniciar Sesión
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true) 

            try {
                val result = repository.loginUser(request)

                if (result != null && result.usuario != null && result.token != null) {
                    _uiState.value = AuthUiState(
                        isAuthenticated = true,
                        user = result.usuario
                    )
                    
                    _userToken.value = result.token
                    saveSessionData(result.usuario, result.token) 

                } else {
                    _uiState.value = AuthUiState(error = result?.mensaje ?: "Credenciales inválidas")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = "Error de conexión: ${e.message}")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                val result = repository.registerUser(request)

                if (result != null && result.usuario != null && result.token != null) {
                    _uiState.value = AuthUiState(
                        isAuthenticated = true,
                        user = result.usuario
                    )
                    _userToken.value = result.token
                    saveSessionData(result.usuario, result.token)

                } else {
                    _uiState.value = AuthUiState(error = result?.mensaje ?: "Fallo en el registro.")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = "Error de conexión: ${e.message}")
            }
        }
    }
    
    private fun saveSessionData(user: User, token: String) {
        println("Sesión guardada - Token: $token, User ID: ${user.id}")
    }
}
