package com.example.mattapp_proyect.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
// --- IMPORTANTE: Se agregó este import ---
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    // 1. Cargar Historial al iniciar
    LaunchedEffect(Unit) {
        userViewModel.fetchHistorial()
    }

    // 2. Observar Estado
    val listaDeHistorial by userViewModel.historial.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showFilterMenu by remember { mutableStateOf(false) }
    var activeFilter by remember { mutableStateOf("Ninguno") }

    // Lógica de filtrado
    val filteredList = remember(searchQuery, activeFilter, listaDeHistorial) {
        val searchResults = if (searchQuery.isEmpty()) {
            listaDeHistorial
        } else {
            listaDeHistorial.filter {
                it.materia.contains(searchQuery, ignoreCase = true) ||
                        it.tipoArchivo.contains(searchQuery, ignoreCase = true)
            }
        }

        when (activeFilter) {
            "Fecha" -> searchResults.sortedByDescending { it.fecha }
            "Materia" -> searchResults.sortedBy { it.materia }
            "Tipo" -> searchResults.sortedBy { it.tipoArchivo }
            else -> searchResults
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar en historial...") },
                        leadingIcon = { Icon(Icons.Default.Search, "Buscar") }
                    )
                },
                actions = {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, "Filtrar")
                    }
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        listOf("Fecha", "Materia", "Tipo", "Ninguno").forEach { filtro ->
                            DropdownMenuItem(
                                text = { Text(filtro) },
                                onClick = {
                                    activeFilter = filtro
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Se usa items importado de androidx.compose.foundation.lazy.items
            items(filteredList) { item ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Materia: ${item.materia}", style = MaterialTheme.typography.titleMedium)
                    Text("Tipo: ${item.tipoArchivo}, Fecha: ${item.fecha}")
                    Divider()
                }
            }
        }
    }
}