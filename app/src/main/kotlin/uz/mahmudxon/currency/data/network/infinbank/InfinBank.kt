package uz.mahmudxon.currency.data.network.infinbank

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class InfinBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.InfinBank.ordinal,
            name = "InfinBank",
            website = "https://infinbank.com/",
            logo = "https://infinbank.com/local/templates/infinbank/favicon/apple-touch-icon.png",
            address = "100029, Toshkent shahri, Mirobod tumani, T. Shevchenko ko‘chasi, 1-uy",
            phone = "140-50-60",
            licens = "75, 15.04.2023",
            inn = "206942764"
        )
    override val url: String
        get() = "https://infinbank.com/uz/private/exchange-rates/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val table = responseAsText.substringAfter("<tbody>").substringBefore("</tbody>")
        val section =
            table.substringAfter("<td class=\"rates-subtitle\" rowspan=\"2\">Ayrboshlash shoxobchasi</td>")
                .substringBefore("<td class=\"rates-subtitle\" rowspan=\"2\">Ilova</td>")
        val buy = section.split(Regex("<td>|</td>")).filter { it.trim().isNotEmpty() }
        val sell =
            section.split(Regex("<td class=\"pt-0\">|</td>")).filter { it.trim().isNotEmpty() }

        val sellValues = ArrayList<Double>()
        val buyValues = ArrayList<Double>()
        buy.forEach {
            try {
                val v = it.replace(" ", "").toDouble()
                sellValues.add(v)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        sell.forEach {
            try {
                val v = it.replace(" ", "").toDouble()
                buyValues.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val filteredValues = ArrayList<Double>()
        filteredValues.addAll(
            sellValues[0], buyValues[0], sellValues[1], buyValues[1], sellValues[3], buyValues[3]
        )

        return filteredValues
    }
}