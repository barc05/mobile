package com.example.mattapp_proyect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "users")
data class User (
    val nombre: String,

    @PrimaryKey
    val correo: String,

    val contraseña: String,

    val fotoUri: String? = null
)
