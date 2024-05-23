package uz.mahmudxon.currency.ui.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import uz.mahmudxon.currency.model.Currency

class NavActions(private val navController: NavHostController) {
    fun navigateToCurrencyDetails(currency: Currency) {
        navController.navigate(Routes.Details.route, bundleOf("currency" to currency))
    }
}

fun NavHostController.navigate(
    route: String,
    bundle: Bundle,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    val destination = graph.findNode(route)
    if (destination != null) {
        if (builder != null)
            navigate(destination.id, bundle, navOptions(builder))
        else navigate(destination.id, bundle)
    }
}
