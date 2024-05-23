package uz.mahmudxon.currency.data.network.aloqabank

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class AloqaBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.ALOQA.ordinal,
            name = "Aloqa Bank",
            website = "https://aloqabank.uz/",
            logo = "https://aloqabank.uz/favicons/favicon-32x32.png"
        )
    override val url: String
        get() = "https://aloqabank.uz/uz/services/exchange-rates/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val values = ArrayList<Double>()
        val table = responseAsText.substringAfter("<table class=\"exchange__table\">")
            .substringBefore("</table>")
        val spanTags = table.split(Regex("<span>|</span>")).filter { it.trim().isNotEmpty() }
        spanTags.forEach { s ->
            try {
                val v = s.toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val filterValues = ArrayList<Double>()
        filterValues.addAll(
            values[0], values[1], values[3], values[4], values[15], values[16]
        )
        return filterValues
    }
}