package com.example.mattapp_proyect.repository

import android.content.Context
import android.net.Uri
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.data.remote.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class UserRepository {

    private val supabase = SupabaseClient.client

    suspend fun login(email: String, pass: String): Boolean {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            true // Login exitoso
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun register(user: User, pass: String): Boolean {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = user.correo
                this.password = pass
                this.data = mapOf("nombre" to user.nombre, "rol" to user.rol)
            }
            insertUserInPublicTable(user.copy(id = supabase.auth.currentUserOrNull()?.id))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    private suspend fun insertUserInPublicTable(user: User) {
        user.id?.let {
             supabase.from("usuarios").insert(user)
        }
    }

    fun getCurrentUserEmail(): String? = supabase.auth.currentUserOrNull()?.email
    fun getCurrentUserId(): String? = supabase.auth.currentUserOrNull()?.id

    suspend fun logout() {
        supabase.auth.signOut()
    }

    suspend fun uploadFile(context: Context, uri: Uri): String? {
        val currentUser = supabase.auth.currentUserOrNull() ?: return null
        val fileName = "${currentUser.id}/${System.currentTimeMillis()}.jpg" // Ejemplo ruta
        
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@withContext null
                
                val bucket = supabase.storage.from("mattapp")
                bucket.upload(fileName, bytes)
                
                // Devolver URL pública
                bucket.publicUrl(fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getFiles(): List<UploadedFile> {
        return try {
            supabase.from("archivos").select().decodeList<UploadedFile>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
