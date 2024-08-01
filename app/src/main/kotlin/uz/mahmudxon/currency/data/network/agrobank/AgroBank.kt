package uz.mahmudxon.currency.data.network.agrobank

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank

class AgroBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.AgroBank.ordinal,
            name = "AgroBank",
            logo = "https://agrobank.uz/f/dist/favicon.ico",
            website = "https://agrobank.uz/",
            address = "100011, Toshkent shahri, Shayxontoxur tumani, Botir Zokirov ko‘chasi, 2A",
            phone = "150-67-65",
            licens = "78, 30.04.2009",
            inn = "201200124"
        )
    override val url: String
        get() = "https://agrobank.uz/api/v1/?action=pages&code=uz%2Fperson%2Fexchange_rates"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val result = ArrayList<Double>()
        val json = Json.parseToJsonElement(responseAsText).jsonObject
        val sections = json["data"]?.jsonObject?.get("sections")?.jsonArray

        if (sections != null) {
            for (section in sections) {
                val blocks = section.jsonObject["blocks"]?.jsonArray
                if (blocks != null) {
                    for (block in blocks) {
                        val items = block.jsonObject["content"]?.jsonObject?.get("items")?.jsonArray
                        items?.forEach {
                            val item = it.jsonObject
                            val buy = item["buy"]?.jsonPrimitive?.int
                            val sale = item["sale"]?.jsonPrimitive?.int
                            result.add(buy?.toDouble() ?: 0.0)
                            result.add(sale?.toDouble() ?: 0.0)
                            if (result.size == 6)
                                return result
                        }
                    }
                }
            }
        }
        return result
    }
}