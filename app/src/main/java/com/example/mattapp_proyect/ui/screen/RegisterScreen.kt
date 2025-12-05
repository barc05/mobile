package com.example.mattapp_proyect.ui.screen

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.ui.Screen
import com.example.mattapp_proyect.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var confirmarContraseña by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("Alumno") }

    val rolesOptions = listOf("Alumno", "Maestro")
    val isLoading by userViewModel.loading.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    // Validaciones simples
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val isPasswordValid = contraseña.length >= 6
    val doPasswordsMatch = contraseña == confirmarContraseña
    val isFormValid = isEmailValid && isPasswordValid && doPasswordsMatch && nombre.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
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
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isEmailValid && correo.isNotEmpty(),
                singleLine = true
            )

            // Contraseña
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña (min 6 chars)") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !isPasswordValid && contraseña.isNotEmpty(),
                singleLine = true
            )

            // Confirmar Contraseña
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmarContraseña,
                onValueChange = { confirmarContraseña = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !doPasswordsMatch && confirmarContraseña.isNotEmpty(),
                singleLine = true
            )

            // Selector de Rol
            Spacer(modifier = Modifier.height(24.dp))
            Text("Soy:", style = MaterialTheme.typography.bodyLarge)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                rolesOptions.forEach { text ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (text == rolSeleccionado),
                                onClick = { rolSeleccionado = text }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == rolSeleccionado),
                            onClick = { rolSeleccionado = text }
                        )
                        Text(text = text, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Registrar
            Button(
                onClick = {
                    userViewModel.registraUsuario(nombre, correo, contraseña, rolSeleccionado)
                    // Navegamos al login para que el usuario ingrese con sus nuevas credenciales
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrar")
                }
            }
        }
    }
}