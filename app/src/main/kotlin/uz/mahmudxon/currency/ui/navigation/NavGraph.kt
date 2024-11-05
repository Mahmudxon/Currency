package uz.mahmudxon.currency.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import uz.mahmudxon.currency.ui.main.MainScreen

@Composable
fun ApplicationNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val startDestination: String = Routes.Main.route
    val navActions = NavActions(navController)

    NavHost(navController = navController, startDestination = startDestination)
    {
        composable(Routes.Main.route) {
            MainScreen()
        }
    }
}