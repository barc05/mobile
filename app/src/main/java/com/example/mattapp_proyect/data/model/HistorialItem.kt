package com.example.mattapp_proyect.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial",
    // Define la "llave foránea" para conectarla al usuario
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["correo"], // PrimaryKey de User
            childColumns = ["userEmail"], // Campo de enlace aquí
            onDelete = ForeignKey.CASCADE // Si se borra el User, se borra su historial
        )
    ]
)
data class HistorialItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userEmail: String,
    val materia: String,
    val tipoArchivo: String,
    val fecha: String
)