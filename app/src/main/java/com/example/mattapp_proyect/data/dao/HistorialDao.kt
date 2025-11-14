package com.example.mattapp_proyect.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mattapp_proyect.data.model.HistorialItem
import kotlinx.coroutines.flow.Flow

interface HistorialDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistorial(item: HistorialItem)

    // Un Flow se actualiza automáticamente
    @Query("SELECT * FROM historial WHERE userEmail = :email ORDER BY fecha DESC")
    fun getHistorialForUser(email: String): Flow<List<HistorialItem>>

}