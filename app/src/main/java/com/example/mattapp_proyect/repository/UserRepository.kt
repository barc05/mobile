package com.example.mattapp_proyect.repository

import android.content.Context
import android.net.Uri
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
import com.example.mattapp_proyect.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserRepository {

    private val supabase = SupabaseClient.client

    // --- AUTENTICACIÓN ---

    suspend fun login(email: String, pass: String): Boolean {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            true
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
                this.data = buildJsonObject {
                    put("nombre", user.nombre)
                    put("rol", user.rol)
                }
            }
            // Copiar ID de Auth a la tabla de usuarios
            val userId = supabase.auth.currentUserOrNull()?.id
            if (userId != null) {
                insertUserInPublicTable(user.copy(id = userId))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun insertUserInPublicTable(user: User) {
        try {
            // "usuarios" debe ser el nombre de tu tabla en Supabase
            supabase.from("usuarios").insert(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentUserId(): String? = supabase.auth.currentUserOrNull()?.id

    // Obtener el objeto User completo desde la base de datos
    suspend fun getCurrentUser(): User? {
        val id = getCurrentUserId() ?: return null
        return try {
            supabase.from("usuarios").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<User>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun logout() {
        supabase.auth.signOut()
    }

    // --- ARCHIVOS (STORAGE) ---

    suspend fun uploadFile(context: Context, uri: Uri): String? {
        val currentUser = supabase.auth.currentUserOrNull() ?: return null
        val fileName = "${currentUser.id}/${System.currentTimeMillis()}.jpg"

        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@withContext null

                val bucket = supabase.storage.from("mattapp")
                bucket.upload(fileName, bytes)
                bucket.publicUrl(fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // --- BASE DE DATOS (CONSULTAS) ---

    suspend fun getFiles(): List<UploadedFile> {
        return try {
            supabase.from("archivos").select().decodeList<UploadedFile>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHistorial(userId: String): List<HistorialItem> {
        return try {
            supabase.from("historial").select {
                filter {
                    eq("usuarioId", userId) // Ajusta "usuarioId" a tu columna real
                }
            }.decodeList<HistorialItem>()
        } catch (e: Exception) {
            emptyList()
        }
    }


    // AGREGA ESTO AL FINAL DE LA CLASE UserRepository
    suspend fun updateUserPhotoUrl(userId: String, url: String) {
        try {
            // Asegúrate de que la columna en tu Supabase se llame "fotoUri" o "foto_uri"
            // Aquí asumo que en tu modelo User la llamaste "fotoUri"
            supabase.from("usuarios").update({
                set("fotoUri", url)
            }) {
                filter {
                    eq("id", userId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
