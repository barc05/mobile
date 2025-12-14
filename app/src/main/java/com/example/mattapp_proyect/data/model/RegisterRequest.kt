package com.example.mattapp_proyect.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val nombre: String,
    val rol: String
)
