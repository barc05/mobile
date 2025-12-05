package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.LoginScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaLogin() {
        composeTestRule.setContent {
            LoginScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        // Verificar título
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()

        // Verificar campos
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()

        // Interactuar: Escribir credenciales
        composeTestRule.onNodeWithText("Correo Electrónico").performTextInput("test@ejemplo.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")

        // Verificar texto escrito
        composeTestRule.onNodeWithText("test@ejemplo.com").assertIsDisplayed()

        // Verificar botones
        composeTestRule.onNodeWithText("Entrar").assertIsDisplayed()
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate aquí").assertIsDisplayed()
    }
}