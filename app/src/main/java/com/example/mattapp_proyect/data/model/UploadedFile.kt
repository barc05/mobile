package com.example.mattapp_proyect.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "files",
    // Define la "llave foránea" para conectarla al usuario
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["correo"], // PrimaryKey de User
            childColumns = ["userEmail"], // Campo de enlace aquí
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UploadedFile (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userEmail: String, // a qué usuario pertenece
    val nombre: String,
    val materia: String
)