package com.example.mattapp_proyect.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mattapp_proyect.ui.Screen
import com.example.mattapp_proyect.viewModel.UserViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {

    val currentUser = userViewModel.loggedInUser.value
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // --- Lógica de tu FileUploadScreen.kt ---
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Scaffold (
        // --- AÑADE ESTA SECCIÓN 'topBar' ---
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
        // --- FIN DE LA SECCIÓN ---
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge)
            //Spacer(modifier = Modifier.height(16.dp))

            // --- Lógica de imagen (de FileUploadScreen.kt) ---
            val painter = rememberAsyncImagePainter(
                imageUri ?: currentUser?.fotoUri // Muestra la nueva Uri o la guardada
            )
            Image(
                painter = painter,
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // --- Nombre del Usuario (del ViewModel) ---
            Text(
                text = currentUser?.nombre ?: "Cargando...",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = currentUser?.correo ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(onClick = { selectImageLauncher.launch("image/*") }) {
                Text("🖼️ Cambiar Foto")
            }

            // Botón para guardar la foto (si se seleccionó una nueva)
            if (imageUri != null && currentUser != null) {
                Button(onClick = {
                    val userActualizado = currentUser.copy(fotoUri = imageUri.toString())
                    userViewModel.updateUser(userActualizado)
                    imageUri = null // Limpiar la selección
                }) {
                    Text("Guardar Foto")
                }
            }

            Divider(Modifier.padding(vertical = 16.dp))

            // --- Opciones de Configuración ---
            Button(
                onClick = { /* TODO: Lógica para cambiar contraseña */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = false // Deshabilitado por ahora
            ) {
                Text("1 - Cambiar Contraseña")
            }

            Button(
                onClick = { /* TODO: Lógica para ajustes */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = false // Deshabilitado por ahora
            ) {
                Text("2 - Ajustes de Notificación")
            }

            Button(
                onClick = {
                    userViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        // Limpiar el stack para que no pueda volver atrás
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("3 - Cerrar Sesión")
            }
        }
    }
}