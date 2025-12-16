package com.example.mattapp_proyect.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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

    // Observar usuario con StateFlow
    val currentUser by userViewModel.loggedInUser.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Imagen
            val painter = rememberAsyncImagePainter(
                imageUri ?: currentUser?.fotoUri
            )
            Image(
                painter = painter,
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // Datos
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

            // Botón Guardar Foto (CORREGIDO)
            if (imageUri != null) {
                Button(onClick = {
                    // --- CAMBIO AQUÍ ---
                    // Antes usabas: userViewModel.addUploadedFile(...)
                    // AHORA USA ESTO:
                    userViewModel.updateUserProfilePhoto(navController.context, imageUri!!)

                    imageUri = null
                }) {
                    Text("Guardar Foto")
                }
            }

            Divider(Modifier.padding(vertical = 16.dp))

            Button(
                onClick = {
                    userViewModel.logout()
                    navController.navigate(Screen.Login.route) {
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