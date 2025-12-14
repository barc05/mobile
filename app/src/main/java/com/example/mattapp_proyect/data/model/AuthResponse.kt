package com.example.mattapp_proyect.data.model

import com.example.mattapp_proyect.data.model.User

data class AuthResponse(
    val mensaje: String,
    val usuario: User?, 
    val token: String? 
)
