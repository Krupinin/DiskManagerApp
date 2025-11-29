package ru.wish.oop_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.wish.oop_android.screens.AddEditDiskScreen
import ru.wish.oop_android.screens.DiskListScreen
import ru.wish.oop_android.ui.theme.Oop_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Oop_androidTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            DiskListScreen(navController)
        }
        composable("add") {
            AddEditDiskScreen(navController)
        }
        composable("edit/{diskId}") { backStackEntry ->
            val diskId = backStackEntry.arguments?.getString("diskId")
            AddEditDiskScreen(navController, diskId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Oop_androidTheme {
        AppNavigation()
    }
}
