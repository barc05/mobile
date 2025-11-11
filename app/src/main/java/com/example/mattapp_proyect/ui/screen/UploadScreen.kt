package com.example.mattapp_proyect.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController){
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var nombre by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var permisoSeleccionado by remember { mutableStateOf("Privado") }
    val permisosOptions = listOf("Privado", "Compartido")

    val selectFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument() // Abre el gestor de archivos
    ) { uri ->
        fileUri = uri
        // Opcional: si 'nombre' está vacío, usa el nombre del archivo
        if (nombre.isEmpty()) {
            fileUri?.lastPathSegment?.let {
                nombre = it.substringBeforeLast(".") // Quita la extensión (ej: .pdf)
            }
        }
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

            // --- SELECCIONAR ARCHIVO ---
            Button(onClick = {
                // Lanza el selector de archivos
                selectFileLauncher.launch(arrayOf("*/*")) // Acepta cualquier tipo
            }) {
                Text("📁 1. Seleccionar Archivo")
            }

            // Muestra el archivo seleccionado
            if (fileUri != null) {
                Text(
                    text = "Archivo: ${fileUri?.lastPathSegment ?: "Desconocido"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Divider()

            // --- AÑADIR DETALLES ---
            Text("2. Añadir Detalles", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Archivo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = materia,
                onValueChange = { materia = it },
                label = { Text("Materia / Carpeta / Etiqueta") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- PERMISOS ---
            Text("Permisos:", style = MaterialTheme.typography.bodyLarge)
            Row(Modifier.fillMaxWidth()) {
                permisosOptions.forEach { text ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (text == permisoSeleccionado),
                                onClick = { permisoSeleccionado = text }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == permisoSeleccionado),
                            onClick = null // El clic se maneja en el 'selectable'
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al fondo

            // --- BOTÓN "SUBIR" ---
            Button(
                onClick = {
                    // TODO: Añadir la lógica de subida real aquí
                    // (ej. llamar a un viewModel.uploadFile(fileUri, nombre, materia, permiso))

                    // De momento, solo volvemos a la pantalla anterior
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth(),
                // El botón solo se activa si se seleccionó un archivo
                enabled = fileUri != null
            ) {
                Text("Subir")
            }
        }
    }
}