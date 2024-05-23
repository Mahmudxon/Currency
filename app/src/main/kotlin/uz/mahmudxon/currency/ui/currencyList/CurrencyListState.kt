package uz.mahmudxon.currency.ui.currencyList

import uz.mahmudxon.currency.model.Currency

data class CurrencyListState(
    val currencies: List<Currency> = emptyList(),
    val isLoading: Boolean = false,
    val errorCode: Int = -1,
    val searchQuery: String = "",
    val selectedCurrency: Currency? = null,
)