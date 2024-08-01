package uz.mahmudxon.currency.data.network.davrbank

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
        val result = ArrayList<Double>()
        val jsonArray = Json.parseToJsonElement(responseAsText).jsonArray
        jsonArray.forEach {
            val item = it.jsonObject
            val buy = item["purchase"]?.jsonPrimitive?.int
            val sale = item["sale"]?.jsonPrimitive?.int
            result.add(buy?.toDouble() ?: 0.0)
            result.add(sale?.toDouble() ?: 0.0)
            // Davr bank is not supports RUB
            if (result.size == 4)
                return result
        }
        return result
    }
}