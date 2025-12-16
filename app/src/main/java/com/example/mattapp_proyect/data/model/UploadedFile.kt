package com.example.mattapp_proyect.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable



@Serializable
data class UploadedFile (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val correo: String, // a qué usuario pertenece
    val nombre: String,
    val materia: String,
    val url: String // CAMPO NUEVO
)