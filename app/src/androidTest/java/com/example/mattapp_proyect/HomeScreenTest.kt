package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.HomeScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaHomeConUsuario() {
        // Simulamos login antes de montar la UI (usando credenciales que sabemos que existen en el Repo mock/api)
        // Nota: Si usas la API real, asegúrate de que este usuario exista o créalo en el test.
        // Si usas el Repo Mock (local), "mateo@test.com" existe por defecto.
        viewModel.loginUsuario("mateo@test.com", "123456")

        composeTestRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        // Esperar a que la UI reaccione (Compose rule espera idle automáticamente, pero el login es asíncrono)
        composeTestRule.waitForIdle()

        // Verificar saludo (Mateo es el nombre del usuario mock)
        // Usamos substring=true por si el texto es "¡Bienvenido, Mateo!"
        composeTestRule.onNodeWithText("Mateo", substring = true).assertIsDisplayed()

        // Verificar botones de navegación
        composeTestRule.onNodeWithText("Ver Historial de Puntuaciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ver Mis Archivos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mi Perfil / Configuración").assertIsDisplayed()

        // Verificar botón de maestro (Mateo es maestro)
        composeTestRule.onNodeWithText("Subir Archivo").assertIsDisplayed()
    }
}