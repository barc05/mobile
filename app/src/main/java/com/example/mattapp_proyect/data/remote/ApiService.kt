package com.example.mattapp_proyect.data.remote

import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    // --- USUARIOS ---
    @GET("usuarios")
    suspend fun getUsers(): List<User>

    @POST("usuarios")
    suspend fun createUser(@Body user: User): UserResponse

    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: String): DeleteResponse

    // --- ARCHIVOS ---
    // Subir archivo para un usuario específico
    @Multipart
    @POST("usuarios/{id}/archivos")
    suspend fun uploadFile(
        @Path("id") userId: String,
        @Part archivo: MultipartBody.Part
    ): FileResponse

    // Obtener archivos de un usuario
    @GET("usuarios/{id}/archivos")
    suspend fun getUserFiles(@Path("id") userId: String): List<UploadedFile>

    // Obtener todos los archivos (para el alumno)
    @GET("archivos")
    suspend fun getAllFiles(): List<UploadedFile>
}

// Respuestas auxiliares de tu API
data class UserResponse(val mensaje: String, val usuarios: List<User>?)
data class DeleteResponse(val mensaje: String)
data class FileResponse(val mensaje: String, val archivo: UploadedFile)