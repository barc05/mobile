package com.example.mattapp_proyect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class User (
    val nombre: String,

    val correo: String,

    val contraseña: String,

    val fotoUri: String? = null
)
