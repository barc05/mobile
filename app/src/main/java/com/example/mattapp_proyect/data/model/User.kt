package com.example.mattapp_proyect.data.model


data class User (
    val id: String? = null,
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val rol: String,
    val fotoUri: String? = null
)
