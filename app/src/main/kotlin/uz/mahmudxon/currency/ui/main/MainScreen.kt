package uz.mahmudxon.currency.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import uz.mahmudxon.currency.ui.currencyDetails.CurrencyDetailsScreen
import uz.mahmudxon.currency.ui.currencyDetails.CurrencyDetailsViewModel
import uz.mahmudxon.currency.ui.currencyList.CurrencyListScreen
import uz.mahmudxon.currency.ui.currencyList.CurrencyListViewmodel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val mainState by mainViewModel.state.collectAsState()

    BackHandler(navigator.canNavigateBack()) {
        if (navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail) {
            mainViewModel.deselectCurrency()
        }
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val viewModel = hiltViewModel<CurrencyListViewmodel>()
                val state by viewModel.state.collectAsState()
                CurrencyListScreen(state = state,
                    onEvent = viewModel::onEvent,
                    selectedCurrency = if (navigator.canNavigateBack()) null
                    else mainState.selectedCurrency,
                    navigateDetails = { item ->
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        mainViewModel.selectCurrency(item)
                    })
            }
        },
        detailPane = {
            AnimatedPane {
                mainState.selectedCurrency?.let {
                    val viewModel = hiltViewModel<CurrencyDetailsViewModel>()
                    viewModel.getCurrency(it)
                    val detailState by viewModel.state.collectAsState()
                    CurrencyDetailsScreen(
                        state = detailState,
                        onEvent = viewModel::onEvent,
                        isCloseIcon = !navigator.canNavigateBack(),
                        onBackClick = {
                            navigator.navigateBack()
                            mainViewModel.deselectCurrency()
                        },
                    )
                }
            }
        },
    )

}