package com.example.mattapp_proyect.viewModel

import android.content.Context
import android.net.Uri
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

class UserViewModel : ViewModel() {

    private val repo = UserRepository()

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

    fun setUserState(user: User) {
        _loggedInUser.value = user
    }

    fun setFilesState(lista: List<UploadedFile>) {
        _files.value = lista
    }

    fun setHistorialState(lista: List<HistorialItem>) {
        _historial.value = lista
    }

    fun loginUsuario(correo: String, contraseña: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val users = repo.getUsers()
                val user = users.find { it.correo == correo && it.contraseña == contraseña }

                if (user != null) {
                    _loggedInUser.value = user
                    fetchUploadedFiles()
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
        val token = _userToken.value
        
        if (token == null) return

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
            _errorMessage.value = "Error: Sesión no válida, por favor reloguearse."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                repo.uploadFile(context, userId, uri, token)
                fetchUploadedFiles() 
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al subir archivo: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
        _files.value = emptyList()
    }

    fun fetchHistorial() {
        val currentUser = _loggedInUser.value ?: return
        val userId = currentUser.id ?: return
        val token = _userToken.value

        if (token == null) return

        viewModelScope.launch {
            _loading.value = true
            try {
                _historial.value = repo.getHistorial(userId, token)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al obtener historial"
            } finally {
                _loading.value = false
            }
        }
    }
}
