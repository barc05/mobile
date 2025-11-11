package com.example.mattapp_proyect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mattapp_proyect.data.dao.UserDao
import com.example.mattapp_proyect.data.model.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Room implementará esta función por nosotros
    abstract fun userDao(): UserDao

    // Patrón Singleton para asegurar que solo haya UNA instancia de la BD
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mathquiz_database"
                )
                    .fallbackToDestructiveMigration() // (Para pruebas, borra y recrea si cambia la versión)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}