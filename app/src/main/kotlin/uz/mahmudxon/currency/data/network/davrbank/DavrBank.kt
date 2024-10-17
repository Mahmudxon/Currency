package uz.mahmudxon.currency.data.network.davrbank

import org.json.JSONArray
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank

class DavrBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.DavrBank.ordinal,
            name = "Davr Bank",
            website = "https://davrbank.uz/",
            logo = "https://davrbank.uz/assets/favicons/favicon-32x32.png",
            address = "100021, Toshkent shahri, Shayxontoxur tumani, Navoiy-Zarqaynar ko‘chasi, Blok “A”",
            phone = "248 35 12",
            licens = "71, 29.09.2001",
            inn = "203709707"
        )
    override val url: String
        get() = "https://apis.davrbank.uz/api/v1/currency/?format=json"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val jsonArray = JSONArray(responseAsText)
        var usdSale = 0.0
        var usdPurchase = 0.0
        var euroSale = 0.0
        var euroPurchase = 0.0

        for (i in 0 until jsonArray.length()) {
            val currency = jsonArray.getJSONObject(i)
            val currencyName = currency.getString("currency_name")

            when (currencyName) {
                "Доллар США" -> {
                    usdSale = currency.getDouble("sale")
                    usdPurchase = currency.getDouble("purchase")
                }

                "ЕВРО" -> {
                    euroSale = currency.getDouble("sale")
                    euroPurchase = currency.getDouble("purchase")
                }
            }
        }

        return listOf(usdPurchase, usdSale, euroPurchase, euroSale)
    }
}