package uz.mahmudxon.currency.data.network.asakabank

import org.json.JSONObject
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank

class AsakaBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.AsakaBank.ordinal,
            name = "Asakabank",
            logo = "https://asakabank.uz/images/new_logo.png",
            website = "https://asakabank.uz/uz/",
            address = "100015, O`zbekiston, Toshkent shahri, Mirobod tumani, Nukus ko'chasi, 67 uy ",
            phone = "+998 (71) 200 55 22",
            inn = "201589828",
            licens = "53, 19.01.1996"
        )
    override val url: String
        get() = "https://back.asakabank.uz/1/currency/?type=asaka&currency_type=individual&page_size=6"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val jsonObject = JSONObject(responseAsText)
        val resultsArray = jsonObject.getJSONArray("results")

        var usdBuy = 0.0
        var usdSell = 0.0
        var euroBuy = 0.0
        var euroSell = 0.0
        var rubleBuy = 0.0
        var rubleSell = 0.0

        for (i in 0 until resultsArray.length()) {
            val currency = resultsArray.getJSONObject(i)
            val currencyName = currency.getString("name")

            when (currencyName) {
                "USD" -> {
                    usdBuy = currency.getDouble("buy") / 100
                    usdSell = currency.getDouble("sell") / 100
                }

                "EUR" -> {
                    euroBuy = currency.getDouble("buy") / 100
                    euroSell = currency.getDouble("sell") / 100
                }

                "RUB" -> {
                    rubleBuy = currency.getDouble("buy") / 100
                    rubleSell = currency.getDouble("sell") / 100
                }
            }
        }

        return listOf(usdBuy, usdSell, euroBuy, euroSell, rubleBuy, rubleSell)
    }
}