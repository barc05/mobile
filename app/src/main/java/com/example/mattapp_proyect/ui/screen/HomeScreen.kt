package com.example.mattapp_proyect.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.ui.Screen
import com.example.mattapp_proyect.viewModel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    // Observamos al usuario logueado desde el StateFlow
    val currentUser by userViewModel.loggedInUser.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostramos el nombre del usuario o un texto por defecto
            Text(
                text = "¡Bienvenido, ${currentUser?.nombre ?: "Usuario"}!",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para ver el historial
            Button(
                onClick = { navController.navigate(Screen.History.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Historial de Puntuaciones")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ver archivos
            Button(
                onClick = { navController.navigate(Screen.Archivos.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Mis Archivos")
            }

            // Botón condicional según el Rol
            if (currentUser?.rol == "Maestro") {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(Screen.Upload.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Subir Archivo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Perfil
            Button(
                onClick = { navController.navigate(Screen.Profile.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mi Perfil / Configuración")
            }
        }
    }
}