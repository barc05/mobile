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
    userViewModel: UserViewModel,
    loadOnInit: Boolean = true
) {
    val listaDeArchivos by userViewModel.files.collectAsState()
    val isLoading by userViewModel.loading.collectAsState()


    LaunchedEffect(Unit) {
        if (loadOnInit && listaDeArchivos.isEmpty()) {
            userViewModel.fetchUploadedFiles()
        }
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
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(archivo.nombre, style = MaterialTheme.typography.titleMedium)
                        Divider()
                    }
                }
            }
        }
    }
}
