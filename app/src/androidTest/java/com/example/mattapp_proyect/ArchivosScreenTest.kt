package com.example.mattapp_proyect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mattapp_proyect.data.model.UploadedFile
import com.example.mattapp_proyect.data.model.User
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
        //. Inyectamos usuario y archivos
        val mockUser = User("1", "Mateo", "mateo@test.com", "123", "Maestro")
        viewModel.setUserState(mockUser)

        val mockFiles = listOf(
            UploadedFile(1, "mateo@test.com", "Guía de Álgebra", "Matemáticas"),
            UploadedFile(2, "mateo@test.com", "Examen Física", "Física")
        )
        viewModel.setFilesState(mockFiles)

        composeTestRule.setContent {
            ArchivosScreen(
                navController = rememberNavController(),
                userViewModel = viewModel,
                loadOnInit = false // Evita que la pantalla intente descargar datos y borre los nuestros
            )
        }

        composeTestRule.waitForIdle()

        // 2. Verificaciones
        composeTestRule.onNodeWithText("Mis Archivos Subidos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Guía de Álgebra", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Materia: Matemáticas", substring = true).assertIsDisplayed()
    }
}