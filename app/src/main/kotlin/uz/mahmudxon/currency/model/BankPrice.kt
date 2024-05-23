package uz.mahmudxon.currency.model

data class BankPrice(
    val bank: Bank,
    val currencyCode: String,
    val date: String,
    val sell: Double,
    val buy: Double
)