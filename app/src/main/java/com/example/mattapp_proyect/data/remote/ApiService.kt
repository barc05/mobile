package com.example.mattapp_proyect.data.remote

import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.data.model.AuthResponse
import com.example.mattapp_proyect.data.model.LoginRequest
import com.example.mattapp_proyect.data.model.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @GET("usuarios")
    suspend fun getUsers(): List<User>

    @GET("usuarios/{id}/historial")
    suspend fun getHistorial(
        @Path("id") userId: String,
        @Header("Authorization") token: String 
    ): List<HistorialItem>

    @POST("usuarios")
    suspend fun createUser(@Body user: User): UserResponse

    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: String): DeleteResponse

    @Multipart
    @POST("usuarios/{id}/archivos")
    suspend fun uploadFile(
        @Path("id") userId: String,
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String 
    ): Response<FileResponse>

    // Obtener archivos de un usuario
    @GET("usuarios/{id}/archivos")
    suspend fun getUserFiles(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): List<UploadedFile>

    // Obtener todos los archivos (para el alumno)
    @GET("archivos")
    suspend fun getAllFiles(): List<UploadedFile>

    // Registro y Inicio de sesion
    @POST("auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>
    
}

    

// Respuestas auxiliares de la Api
data class UserResponse(val mensaje: String, val usuarios: List<User>?)
data class DeleteResponse(val mensaje: String)
data class FileResponse(val mensaje: String, val archivo: UploadedFile)
