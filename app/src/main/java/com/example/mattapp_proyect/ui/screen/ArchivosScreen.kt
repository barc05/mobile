package com.example.mattapp_proyect.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.viewModel.UserViewModel
import com.example.mattapp_proyect.data.model.UploadedFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    // 1. Obtén la lista de archivos del ViewModel
    val listaDeArchivos by userViewModel.getUploadedFilesForUser().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Archivos Subidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        // 2. Muestra la lista
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (listaDeArchivos.isEmpty()) {
                item {
                    Text(
                        text = "Aún no has subido archivos.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            items(listaDeArchivos) { archivo ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(archivo.nombre, style = MaterialTheme.typography.titleMedium)
                    Text("Materia: ${archivo.materia}")
                    // Como el archivo no es real, solo mostramos su URI simulada
                    Text("Ubicación: ${archivo.fileUri}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}