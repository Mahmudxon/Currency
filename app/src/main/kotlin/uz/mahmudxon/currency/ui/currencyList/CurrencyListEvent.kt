package uz.mahmudxon.currency.ui.currencyList

import uz.mahmudxon.currency.model.Currency

sealed class CurrencyListEvent {
    data class SelectCurrency(val currency: Currency) : CurrencyListEvent()
    data object OnRefresh : CurrencyListEvent()
    data class Search(val query: String) : CurrencyListEvent()
}