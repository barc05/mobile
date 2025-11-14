package com.example.mattapp_proyect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mattapp_proyect.data.dao.HistorialDao
import com.example.mattapp_proyect.data.dao.UploadedFileDao
import com.example.mattapp_proyect.data.dao.UserDao
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User


@Database(entities = [User::class, HistorialItem::class, UploadedFile::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){

    abstract fun userDao(): UserDao
    abstract fun historialDao(): HistorialDao
    abstract fun uploadedFileDao(): UploadedFileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "mattapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}