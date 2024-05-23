package uz.mahmudxon.currency.data.network.sqb

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class Sqb(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.SQB.ordinal,
            name = "SQB",
            website = "https://www.sqb.uz/",
            logo = "https://www.sqb.uz/local/templates/sqb/images/favicon.png"
        )
    override val url: String
        get() = "https://www.sqb.uz/api/site-kurs-api/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val values = ArrayList<Double>()
        val array =
            Json.parseToJsonElement(responseAsText).jsonObject["data"]!!.jsonObject["offline"]!!.jsonArray
        array.forEach {
            val item = it.jsonObject
            val buy = (item["buy"]?.jsonPrimitive?.int?.toDouble() ?: 0.0) / 100.0
            val sale = (item["sell"]?.jsonPrimitive?.int?.toDouble() ?: 0.0) / 100.0
            values.add(buy)
            values.add(sale)
        }

        val filterValues = ArrayList<Double>()
        filterValues.addAll(
            values[8], values[9], values[10], values[11], values[2], values[3]
        )
        return filterValues
    }
}