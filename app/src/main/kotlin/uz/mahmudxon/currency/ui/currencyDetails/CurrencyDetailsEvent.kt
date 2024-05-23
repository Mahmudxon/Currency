package uz.mahmudxon.currency.ui.currencyDetails

sealed class CurrencyDetailsEvent {
    data object Refresh : CurrencyDetailsEvent()
    data class ForeignCurrencyInput(val value: String) : CurrencyDetailsEvent()
    data class LocalCurrencyInput(val value: String) : CurrencyDetailsEvent()
    data object SwitchCurrency : CurrencyDetailsEvent()
}