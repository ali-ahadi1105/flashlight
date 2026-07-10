package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundManager.init()
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(repository = GameRepository(applicationContext))
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }
}

@Composable
fun AppNavigation(repository: GameRepository) {
    val navController = rememberNavController()
    
    val viewModelFactory = remember {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameViewModel(repository) as T
            }
        }
    }
    
    val viewModel: GameViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuScreen(
                onPlayClick = { navController.navigate("level_select") }
            )
        }
        composable("level_select") {
            LevelSelectScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLevelSelected = { levelId ->
                    viewModel.loadLevel(levelId)
                    navController.navigate("gameplay")
                }
            )
        }
        composable("gameplay") {
            GameplayScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNextLevel = {
                    val currentId = viewModel.gameState.value.currentLevel?.id ?: 1
                    val nextId = currentId + 1
                    viewModel.loadLevel(nextId)
                }
            )
        }
    }
}
