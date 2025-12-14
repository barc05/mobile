package com.example.mattapp_proyect.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseClient {
    
    // credenciales
    private const val SUPABASE_URL = "https://przfxmzrsiclmucjrkao.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_eIRdjBKG9K8BCQfiuYk2Ng_JgDxRFz8"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth) // Autenticación
        install(Postgrest) // Base de datos
        install(Storage) // Archivos

        // Configuración para convertir JSON a Objetos automáticamente
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }
}
