package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.ArchivosScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArchivosScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaArchivos() {
        // Login
        viewModel.loginUsuario("mateo@test.com", "123456")

        composeTestRule.setContent {
            ArchivosScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        // Verificar título
        composeTestRule.onNodeWithText("Mis Archivos Subidos").assertIsDisplayed()

        composeTestRule.onNodeWithText("Guía de Álgebra", substring = true).assertIsDisplayed()
    }
}