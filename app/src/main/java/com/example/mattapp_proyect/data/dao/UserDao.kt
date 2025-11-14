package com.example.mattapp_proyect.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mattapp_proyect.data.model.User

@Dao
interface UserDao {
    // Usamos 'ABORT' para que falle si el email ya existe
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE correo = :correo LIMIT 1")
    suspend fun getUserByCorreo(correo: String): User?

    @Update
    suspend fun updateUser(user: User)
}