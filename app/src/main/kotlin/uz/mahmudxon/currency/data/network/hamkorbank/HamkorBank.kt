package uz.mahmudxon.currency.data.network.hamkorbank

import kotlinx.serialization.json.Json
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank


class HamkorBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.HamkorBank.ordinal,
            name = "Hamkor Bank",
            website = "https://www.hamkorbank.uz/",
            logo = "file:///android_asset/bank/hamkorbank.webp",
            address = "170119, Andijon shahri, Bobur shoh ko‘chasi, 85-uy",
            phone = "1-200-200",
            licens = "64, 29.07.2000",
            inn = "200242936"
        )
    override val url: String
        get() = "https://api-dbo.hamkorbank.uz/webflow/v1/exchanges"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val jsonData = Json.decodeFromString<Response>(responseAsText)
        val result = mutableListOf<Double>()

        jsonData.data.forEach { currency ->
            when (currency.currency_char) {
                "USD", "EUR", "RUB" -> {
                    result.add(currency.buying_rate / 100)
                    result.add(currency.selling_rate / 100)
                }
            }
        }
        return result
    }
}