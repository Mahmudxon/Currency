package uz.mahmudxon.currency.data.network.ofb

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class Ofb(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.Ofb.ordinal,
            name = "OFB",
            logo = "https://ofb.uz/local/dist/apple-touch-icon.png",
            website = "https://ofb.uz/"
        )
    override val url: String
        get() = "https://ofb.uz/uz/about/kurs-obmena-valyut/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val table =
            responseAsText.substringAfter("<div class=\"table\">").substringBefore("</table>")

        val findValues = table.split(Regex("class=\"currency\">|</td>"))
            .filter { it.trim().isNotEmpty() }
        val values = ArrayList<Double>()
        findValues.forEach {
            try {
                val v = it.replace("<br>", "").toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val filterValues = ArrayList<Double>()
        filterValues.addAll(
            values[2], values[1], values[5], values[4], values[17], values[16]
        )
        return filterValues
    }
}