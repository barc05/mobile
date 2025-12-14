package com.example.mattapp_proyect.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mattapp_proyect.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_session")

class SessionManager(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    // Guardar sesión
    suspend fun saveSession(token: String, user: User) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = user.id ?: ""
            preferences[USER_NAME_KEY] = user.nombre
            preferences[USER_EMAIL_KEY] = user.correo
            preferences[USER_ROLE_KEY] = user.rol
        }
    }

    // Obtener Token
    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // Obtener Usuario guardado
    val user: Flow<User?> = context.dataStore.data.map { preferences ->
        val id = preferences[USER_ID_KEY]
        val name = preferences[USER_NAME_KEY]
        val email = preferences[USER_EMAIL_KEY]
        val role = preferences[USER_ROLE_KEY]

        if (!id.isNullOrEmpty() && !name.isNullOrEmpty() && !email.isNullOrEmpty() && !role.isNullOrEmpty()) {
            User(id, name, email, "", role)
        } else {
            null
        }
    }

    // Cerrar sesión
    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}