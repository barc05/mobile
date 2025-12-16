package com.example.mattapp_proyect.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable



@Serializable
data class UploadedFile (

    val usuario_id: String,
    val nombre: String,
    val url: String // <-- OK
)