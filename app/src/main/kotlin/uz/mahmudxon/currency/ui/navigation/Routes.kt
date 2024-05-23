package uz.mahmudxon.currency.ui.navigation

sealed class Routes(val route: String) {
    data object Main : Routes("main")
    data object Details : Routes("details")
    data object Settings : Routes("settings")
}