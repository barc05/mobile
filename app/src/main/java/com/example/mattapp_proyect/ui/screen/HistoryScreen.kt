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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.viewModel.UserViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    // Asumimos que pasaste el viewModel desde AppNavigation
    userViewModel: UserViewModel
) {

    // --- 1. ESTADOS (Búsqueda y filtro se mantienen) ---
    var searchQuery by remember { mutableStateOf("") }
    var showFilterMenu by remember { mutableStateOf(false) }
    var activeFilter by remember { mutableStateOf("Ninguno") }

    // --- 1.b OBTENER DATOS (¡MUCHO MÁS SIMPLE!) ---
    // Llama a la función simulada del ViewModel. No es Flow, ni suspend.
    val listaDeHistorial by userViewModel.getHistorialParaUsuario().collectAsState(initial = emptyList())


    // --- 2. LÓGICA DE FILTRADO (Funciona igual que antes) ---

    val filteredList = remember(searchQuery, activeFilter, listaDeHistorial) {
        // Primero, aplica la búsqueda (del TextField)
        val searchResults = if (searchQuery.isEmpty()) {
            listaDeHistorial
        } else {
            listaDeHistorial.filter {
                it.materia.contains(searchQuery, ignoreCase = true) ||
                        it.tipoArchivo.contains(searchQuery, ignoreCase = true)
            }
        }

        // Segundo, aplica el filtro (del DropdownMenu)
        when (activeFilter) {
            "Fecha" -> searchResults.sortedByDescending { it.fecha }
            "Materia" -> searchResults.sortedBy { it.materia }
            "Tipo" -> searchResults.sortedBy { it.tipoArchivo }
            else -> searchResults // "Ninguno"
        }
    }


    // --- 3. INTERFAZ DE USUARIO (Como en tus otras pantallas) ---
    Scaffold(
        topBar = {
            TopAppBar(
                // --- BOTÓN VOLVER (Implementado antes) ---
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },

                // --- BARRA DE BÚSQUEDA (El 'title' es ahora un TextField) ---
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar en historial...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    )
                },

                // --- BOTÓN DE FILTRO (Acciones) ---
                actions = {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }

                    // --- MENÚ DESPLEGABLE DE FILTROS ---
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Filtrar por: Fecha") },
                            onClick = {
                                activeFilter = "Fecha"
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Filtrar por: Materia") },
                            onClick = {
                                activeFilter = "Materia"
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Filtrar por: Tipo") },
                            onClick = {
                                activeFilter = "Tipo"
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Quitar Filtro") },
                            onClick = {
                                activeFilter = "Ninguno"
                                showFilterMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        // --- 4. LISTA DE RESULTADOS ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(filteredList) { item ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Materia: ${item.materia}", style = MaterialTheme.typography.titleMedium)
                    Text("Tipo: ${item.tipoArchivo}, Fecha: ${item.fecha}")
                }
            }
        }
    }
}