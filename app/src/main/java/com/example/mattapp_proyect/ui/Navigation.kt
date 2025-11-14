package com.example.mattapp_proyect.ui

import androidx.compose.runtime.Composable
import com.example.mattapp_proyect.ui.screen.LoginScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mattapp_proyect.ui.screen.RegisterScreen
import com.example.mattapp_proyect.viewModel.UserViewModel
import com.example.mattapp_proyect.ui.screen.HomeScreen
import com.example.mattapp_proyect.ui.screen.ProfileScreen
import androidx.compose.material3.Text
import com.example.mattapp_proyect.ui.screen.HistoryScreen
import com.example.mattapp_proyect.ui.screen.UploadScreen
import com.example.mattapp_proyect.ui.screen.ArchivosScreen

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
    object History : Screen("history_screen")

    object Archivos : Screen("archivos_screen")

    object Upload : Screen("upload_screen")
    object Profile : Screen("profile_screen") // NUEVA RUTA
}

/**
 * Este es el "controlador" principal que decide qué pantalla mostrar.
 */
@Composable
fun AppNavigation(userViewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // --- Pantalla de Inicio de Sesión ---
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController,
                        userViewModel = userViewModel
            )
        }
        // --- Pantalla de Registro ---
        composable(route = Screen.Register.route) {
            // (Próximo paso: construir esta pantalla)
            RegisterScreen(navController = navController,
                            userViewModel = userViewModel)
        }

        // --- Pantalla Principal (Menú) ---
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController,
                userViewModel = userViewModel)

        }

        // --- NUEVA PANTALLA DE PERFIL ---
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, userViewModel = userViewModel)
        }

        // --- STUBS PARA CORREGIR ERRORES ---

        composable(route = Screen.History.route) {
            HistoryScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(route = Screen.Upload.route) {
            UploadScreen(
                navController = navController,
                userViewModel = userViewModel
                )
        }

        composable(route = Screen.Archivos.route) {
            ArchivosScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

    }
}