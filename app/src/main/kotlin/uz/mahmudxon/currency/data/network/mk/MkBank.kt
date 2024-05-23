package uz.mahmudxon.currency.data.network.mk

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class MkBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.MkBank.ordinal,
            name = "MKBank",
            website = "https://mkbank.uz/",
            logo = "https://mkbank.uz/favicon.ico?v3"
        )
    override val url: String
        get() = "https://mkbank.uz/uz/services/exchange-rates/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val table = responseAsText.substringAfter("<table class=\"exchange__table\">")
            .substringBefore("</table>")
        val columns = table.split(Regex("<td>|</td>")).filter { it.trim().isNotEmpty() }
        val values = ArrayList<Double>()
        columns.forEach {
            try {
                val v = it.toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val filterValues = ArrayList<Double>()
        filterValues.addAll(
            values[0], values[1], values[3], values[4]
        )
        return filterValues
    }
}