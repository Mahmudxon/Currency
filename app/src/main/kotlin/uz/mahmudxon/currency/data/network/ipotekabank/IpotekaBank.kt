package uz.mahmudxon.currency.data.network.ipotekabank

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll
import uz.mahmudxon.currency.util.dlog

class IpotekaBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.IpotekaBank.ordinal,
            name = "IpotekaBank",
            website = "https://ipoteka.uz/",
            logo = "https://www.ipotekabank.uz/bitrix/templates/corp_services_blue_copy/favicon.ico"
        )
    override val url: String
        get() = "https://ipotekabank.uz/uz/currency/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val table =
            responseAsText.substringAfter("<table class=\"table table-hover table-striped\">")
                .substringBefore("</table>")
        val spanTags = table.split(Regex("<span .*?>|</span>")).filter { it.trim().isNotEmpty() }
        dlog(spanTags)
        val values = ArrayList<Double>()
        for (spanTag in spanTags) {
            try {
                val v = spanTag.replace(" ", "").replace(",", ".").toDouble()
                values.add(v)
            } catch (e: Exception) {
                   e.printStackTrace()
            }
        }
        //        0        1          2         3         4        5        6        7        8        9    10     11    12      13     14
        // all = [12690.0, 12785.0, 12724.72, 12700.0, 13850.0, 13798.69, 15050.0, 16250.0, 16196.02, 75.0, 83.0, 81.34, 100.0, 145.0, 141.08]
        // filterValues = [ 12 690,  12 785 ,  12 700 ,  13 850,  100 ,  145 ]
        val filterValues = ArrayList<Double>()
        filterValues.addAll(
            values[0], values[1], values[3], values[4], values[12], values[13]
        )
        return filterValues
    }
}