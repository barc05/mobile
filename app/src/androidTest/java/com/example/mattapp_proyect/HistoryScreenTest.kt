package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.HistoryScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaHistorial() {
        // Login para cargar historial asociado
        viewModel.loginUsuario("mateo@test.com", "123456")

        composeTestRule.setContent {
            HistoryScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        // Barra de búsqueda
        composeTestRule.onNodeWithText("Buscar en historial...").assertIsDisplayed()

        // Verificar items del historial (Datos mock: Matemáticas, Historia)
        composeTestRule.onNodeWithText("Matemáticas", substring = true).assertIsDisplayed()

        // Filtro de búsqueda
        composeTestRule.onNodeWithText("Buscar en historial...").performTextInput("Historia")
        composeTestRule.onNodeWithText("Historia", substring = true).assertIsDisplayed()
    }
}