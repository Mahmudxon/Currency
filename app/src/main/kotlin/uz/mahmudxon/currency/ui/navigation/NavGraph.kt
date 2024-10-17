package uz.mahmudxon.currency.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import uz.mahmudxon.currency.ui.currencyDetails.CurrencyDetailsScreen
import uz.mahmudxon.currency.ui.currencyDetails.CurrencyDetailsViewModel
import uz.mahmudxon.currency.ui.currencyList.CurrencyListScreen
import uz.mahmudxon.currency.ui.currencyList.CurrencyListViewmodel

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
            val vm = hiltViewModel<CurrencyListViewmodel>()
            val mainState = vm.state.collectAsState()
            CurrencyListScreen(
                state = mainState.value,
                onEvent = vm::onEvent,
                navigateDetails = navActions::navigateToCurrencyDetails
            )
        }

        composable(
            Routes.Details.route
        ) {
            val vm = hiltViewModel<CurrencyDetailsViewModel>()
            val detailsState = vm.state.collectAsState()
            CurrencyDetailsScreen(
                state = detailsState.value,
                onEvent = vm::onEvent,
                onBackClick = navController::popBackStack
            )
        }
    }
}