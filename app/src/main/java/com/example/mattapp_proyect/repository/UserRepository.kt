package com.example.mattapp_proyect.repository

import android.content.Context
import android.net.Uri
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.data.remote.ApiClient
import com.example.mattapp_proyect.utils.uriToMultipart

class UserRepository {

    private val api = ApiClient.service

    // --- Usuarios ---
    suspend fun getUsers(): List<User> = api.getUsers()

    suspend fun createUser(user: User): User? {
        val response = api.createUser(user)
        return response.usuarios?.firstOrNull()
    }

    suspend fun deleteUser(id: String) = api.deleteUser(id)

    // --- Archivos ---

    // Lógica para decidir qué archivos mostrar según el rol
    suspend fun getFilesForUser(userId: String?, rol: String): List<UploadedFile> {
        return if (rol == "Alumno") {
            api.getAllFiles()
        } else {
            if (userId != null) api.getUserFiles(userId) else emptyList()
        }
    }

    // Subir archivo usando MultipartUtils
    suspend fun uploadFile(context: Context, userId: String, uri: Uri) {
        val part = uriToMultipart(context, uri, "archivo")
        api.uploadFile(userId, part)
    }

    suspend fun getHistorial(userId: String): List<HistorialItem> {
        return api.getHistorial(userId)
    }
}