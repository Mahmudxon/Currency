package uz.mahmudxon.currency.data.network.xb

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank

class XalqBanki(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.XB.ordinal,
            name = "Xalq Banki",
            website = "https://xb.uz/",
            logo = "https://xb.uz/favicon.ico",
            address = "100096, Toshkent shahri, Chilonzor tumani, Qatortol ko‘chasi, 46-uy",
            phone = " 1106",
            licens = "25, 22.10.1993",
            inn = "207215726"
        )
    override val url: String
        get() = "https://xb.uz/api/v1/external/client/default?_f=json&_l=uz"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val values = ArrayList<Double>()
        val jsonArray = Json.parseToJsonElement(responseAsText).jsonArray
        jsonArray.forEach {
            val o = it.jsonObject
            val buy = o["BUYING_RATE"]?.jsonPrimitive?.content?.replace(" ", "")
            val sale = o["SELLING_RATE"]?.jsonPrimitive?.content?.replace(" ", "")
            values.add(buy?.toDouble() ?: 0.0)
            values.add(sale?.toDouble() ?: 0.0)
            if (values.size == 4)
                return values
        }
        return values
    }
}