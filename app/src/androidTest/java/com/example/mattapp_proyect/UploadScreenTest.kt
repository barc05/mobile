package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.UploadScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UploadScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaSubirArchivo() {
        // Login como maestro
        viewModel.loginUsuario("mateo@test.com", "123456")

        composeTestRule.setContent {
            UploadScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        // Título
        composeTestRule.onNodeWithText("Subir Archivo").assertIsDisplayed()

        // Botón selección
        composeTestRule.onNodeWithText("1. Seleccionar Archivo", substring = true).assertIsDisplayed()

        // Verificar botón subir (debería estar deshabilitado al inicio o visible)
        composeTestRule.onNodeWithText("Subir").assertIsDisplayed()
    }
}