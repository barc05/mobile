package com.example.mattapp_proyect.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    // La nueva API no usa nombre/materia, pero mantenemos los campos visualmente si quieres
    var nombre by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }

    val context = LocalContext.current // Necesario para leer el archivo

    val selectFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        fileUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subir Archivo") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(onClick = { selectFileLauncher.launch("*/*") }) {
                Text("📁 1. Seleccionar Archivo")
            }

            if (fileUri != null) {
                Text(
                    text = "Archivo seleccionado",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Divider()

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (fileUri != null) {
                        // CORRECCIÓN: Llamada a la nueva firma addUploadedFile(context, uri)
                        userViewModel.addUploadedFile(context, fileUri!!)
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = fileUri != null
            ) {
                Text("Subir")
            }
        }
    }
}