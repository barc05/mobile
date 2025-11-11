package com.example.mattapp_proyect.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.ui.Screen
import com.example.mattapp_proyect.viewModel.UserViewModel

@Composable
fun HomeScreen(navController: NavController,
               userViewModel: UserViewModel) {

    val currentUser = userViewModel.loggedInUser.value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Bienvenido, ${currentUser?.nombre ?: ""}!", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para iniciar el cuestionario
            Button(
                onClick = { navController.navigate(Screen.Quiz.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Empezar Cuestionario")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ver el historial
            Button(
                onClick = { navController.navigate(Screen.History.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Historial de Puntuaciones")
            }

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

            Button(
                onClick = { navController.navigate(Screen.Profile.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mi Perfil / Configuración")
            }
        }
    }
}
