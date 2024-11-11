package uz.mahmudxon.currency.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.ui.MainActivity

@Composable
fun getCurrencyName(currency: Currency): String {
    val activity = LocalContext.current as MainActivity
    return activity.currencyNameMap[currency.code] ?: currency.name
}