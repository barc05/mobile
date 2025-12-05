package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.data.model.HistorialItem
import com.example.mattapp_proyect.data.model.User
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
        val mockUser = User("id1", "Mateo", "mateo@test.com", "123", "Maestro")
        viewModel.setUserState(mockUser)

        val mockHistory = listOf(
            HistorialItem(1, "mateo@test.com", "Matemáticas", "Examen", "2023-11-10"),
            HistorialItem(2, "mateo@test.com", "Historia", "Tarea", "2023-11-11")
        )
        viewModel.setHistorialState(mockHistory)

        composeTestRule.setContent {
            HistoryScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        // Verifica barra de búsqueda
        composeTestRule.onNodeWithText("Buscar en historial...").assertIsDisplayed()

        // Verificar que APARECE el item inyectado
        composeTestRule.onNodeWithText("Matemáticas", substring = true).assertIsDisplayed()

        // Escribir en búsqueda
        composeTestRule.onNodeWithText("Buscar en historial...").performTextInput("Historia")

        // Verificar que se filtra/muestra Historia
        composeTestRule.onNodeWithText("Materia: Historia", substring = true).assertIsDisplayed()
    }
}