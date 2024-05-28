package uz.mahmudxon.currency.ui.currencyDetails

import uz.mahmudxon.currency.model.BankPrice

sealed class CurrencyDetailsEvent {
    data object Refresh : CurrencyDetailsEvent()
    data class ForeignCurrencyInput(val value: String) : CurrencyDetailsEvent()
    data class LocalCurrencyInput(val value: String) : CurrencyDetailsEvent()
    data object SwitchCurrency : CurrencyDetailsEvent()
    data class CurrencyPriceClick(val price: BankPrice) : CurrencyDetailsEvent()
    data object OnCurrencyInfoDismissRequest : CurrencyDetailsEvent()
}