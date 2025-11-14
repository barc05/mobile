package com.example.mattapp_proyect.ui.screen

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mattapp_proyect.ui.Screen
import com.example.mattapp_proyect.viewModel.UserViewModel
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel) {

    // --- 1. GESTIÓN DE ESTADO (IL2.2) ---
    // Estados para guardar lo que el usuario escribe
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var confirmarContraseña by remember { mutableStateOf("") }

    var rolSeleccionado by remember { mutableStateOf("Alumno") }
    val rolesOptions = listOf("Alumno", "Maestro")

    // --- 2. LÓGICA DE VALIDACIÓN (IL2.1) ---
    // Lógica de control que se ejecuta cada vez que un estado cambia
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val isPasswordValid = contraseña.length >= 6
    val doPasswordsMatch = contraseña == confirmarContraseña


    // El botón "Guardar" solo se activa si TODO es válido
    val isFormValid = isEmailValid && isPasswordValid && doPasswordsMatch


    // --- 3. INTERFAZ DE USUARIO (IL2.1) ---
    Scaffold (
        // --- AÑADE LA BARRA SUPERIOR CON BOTÓN DE VOLVER ---
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                isError = nombre.isEmpty() && correo.isNotEmpty() // Validación simple
            )



            // --- Campo de Email ---
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                // Retroalimentación visual: se pone rojo si es inválido
                isError = !isEmailValid && correo.isNotEmpty()
            )
            if (!isEmailValid && correo.isNotEmpty()) {
                Text(
                    text = "Por favor, introduce un correo válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Contraseña ---
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                // Oculta la contraseña
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !isPasswordValid && contraseña.isNotEmpty()
            )
            if (!isPasswordValid && contraseña.isNotEmpty()) {
                Text(
                    text = "La contraseña debe tener al menos 6 caracteres",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Confirmar Contraseña ---
            OutlinedTextField(
                value = confirmarContraseña,
                onValueChange = { confirmarContraseña = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !doPasswordsMatch && confirmarContraseña.isNotEmpty()
            )
            if (!doPasswordsMatch && confirmarContraseña.isNotEmpty()) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.height(16.dp))
            Text("Soy:", style = MaterialTheme.typography.bodyLarge)
            Row(Modifier.fillMaxWidth()) {
                rolesOptions.forEach { text ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (text == rolSeleccionado),
                                onClick = { rolSeleccionado = text }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == rolSeleccionado),
                            onClick = null
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

            Spacer(modifier = Modifier.height(16.dp))
            Text("Soy:", style = MaterialTheme.typography.bodyLarge)
            Row(Modifier.fillMaxWidth()) {
                rolesOptions.forEach { text ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (text == rolSeleccionado),
                                onClick = { rolSeleccionado = text }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == rolSeleccionado),
                            onClick = null
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

            Spacer(modifier = Modifier.height(32.dp))

            // --- 4. ---
            Button(
                onClick = {
                    userViewModel.registraUsuario(nombre, correo, contraseña,rolSeleccionado)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },

                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}