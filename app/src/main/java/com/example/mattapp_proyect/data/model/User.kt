package com.example.mattapp_proyect.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class User (
    val id: String? = null,
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val rol: String,
    val fotoUri: String? = null
)
