package com.example.mattapp_proyect.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mattapp_proyect.data.model.UploadedFile
import kotlinx.coroutines.flow.Flow

interface UploadedFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: UploadedFile)

    // Trae los archivos de un usuario específico
    @Query("SELECT * FROM files WHERE userEmail = :email")
    fun getFilesForUser(email: String): Flow<List<UploadedFile>>

    // Trae los archivos de un rol específico (para los alumnos)
    @Query("SELECT * FROM files WHERE userEmail IN (SELECT correo FROM users WHERE rol = :rol)")
    fun getFilesForRole(rol: String): Flow<List<UploadedFile>>
}