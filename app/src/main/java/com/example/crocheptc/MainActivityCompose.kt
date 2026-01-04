package com.example.crocheptc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        
        // Tela Principal (Escolha Login/Cadastro)
        composable("main") {
            MainScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // Tela de Login
        composable("login") {
            // Se precisar passar callbacks de navegação para o LoginScreen, 
            // atualize a assinatura do LoginScreen para receber lambda: onLoginSuccess
            // Por enquanto vamos assumir navegação manual ou mock
            LoginScreen()
        }

        // Tela de Registro
        composable("register") {
            RegisterScreen()
        }

        // Tela de Boas Vindas (Home)
        composable("welcome") {
            WelcomeScreen()
        }
    }
}
