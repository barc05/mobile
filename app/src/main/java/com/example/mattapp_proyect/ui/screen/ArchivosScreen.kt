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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    // Usamos los estados del ViewModel corregido
    val listaDeArchivos by userViewModel.files.collectAsState()
    val isLoading by userViewModel.loading.collectAsState()

    // Cargar archivos al entrar a la pantalla
    LaunchedEffect(Unit) {
        userViewModel.fetchUploadedFiles()
    }

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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Usamos solo los campos que existen en Supabase
                            Text(text = archivo.nombre, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "URL: ${archivo.url}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
