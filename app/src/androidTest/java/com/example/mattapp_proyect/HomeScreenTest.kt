package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.data.model.User
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
        // 1. Inyectar estado de usuario directamente (sin red)
        val mockUser = User(
            id = "1",
            nombre = "Mateo",
            correo = "mateo@test.com",
            contraseña = "123",
            rol = "Maestro"
        )
        viewModel.setUserState(mockUser)

        composeTestRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        // 2. Verificar saludo
        composeTestRule.onNodeWithText("Mateo", substring = true).assertIsDisplayed()

        // 3. Verificar botones
        composeTestRule.onNodeWithText("Ver Historial de Puntuaciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ver Mis Archivos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mi Perfil / Configuración").assertIsDisplayed()

        // 4. Verificar botón de maestro (visible porque el rol es Maestro)
        composeTestRule.onNodeWithText("Subir Archivo").assertIsDisplayed()
    }
}