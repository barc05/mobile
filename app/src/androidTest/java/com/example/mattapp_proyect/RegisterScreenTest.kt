package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.RegisterScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaRegistro() {
        composeTestRule.setContent {
            RegisterScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        // Verificar título
        composeTestRule.onNodeWithText("Crear Cuenta").assertIsDisplayed()

        // Verificar campos de texto
        composeTestRule.onNodeWithText("Nombre Completo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirmar Contraseña").assertIsDisplayed()

        // Interactuar
        composeTestRule.onNodeWithText("Nombre Completo").performTextInput("Estudiante Nuevo")
        composeTestRule.onNodeWithText("Estudiante Nuevo").assertIsDisplayed()

        // Verificar opciones de Rol
        composeTestRule.onNodeWithText("Alumno").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Maestro").assertIsDisplayed()

        // Verificar botón
        composeTestRule.onNodeWithText("Registrar").assertIsDisplayed()
    }
}