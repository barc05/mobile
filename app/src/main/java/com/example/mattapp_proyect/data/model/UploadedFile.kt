package com.example.mattapp_proyect.data.model

data class UploadedFile (
    val id: Int,
    val userEmail: String, // Para saber a qué usuario pertenece
    val nombre: String,
    val materia: String,
    val fileUri: String // La 'dirección' (aunque sea simulada)
)