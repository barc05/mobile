package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.ui.screen.ProfileScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = UserViewModel()

    @Test
    fun testVistaPerfil() {
        // Login previo
        viewModel.loginUsuario("mateo@test.com", "123456")

        composeTestRule.setContent {
            ProfileScreen(
                navController = rememberNavController(),
                userViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        // Verificar datos
        composeTestRule.onNodeWithText("Mateo").assertIsDisplayed()
        composeTestRule.onNodeWithText("mateo@test.com").assertIsDisplayed()

        // Verificar opciones
        composeTestRule.onNodeWithText("Cambiar Foto", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Cerrar Sesión", substring = true).assertIsDisplayed()
    }
}