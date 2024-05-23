package uz.mahmudxon.currency.data.network.nbu

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank

class Nbu(
    networkClient: NetworkClient
) : CommercialBank(networkClient) {

    override val bank: Bank
        get() = Bank(
            id = Bank.Id.NBU.ordinal,
            name = "NBU",
            website = "https://nbu.uz/",
            logo = "file:///android_asset/bank/nbu.webp"
        )
    override val url: String
        get() = "https://nbu.uz/uz/exchange-rates/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        var table = responseAsText.substringAfter("<table>").substringBefore("</table>")
        // remove comments <!-- something -->
        table = table.replace("<!--.*?-->".toRegex(), "")

        val rowTags = table.split(Regex("<tr>|</tr>")).filter { it.trim().isNotEmpty() }

        val values = ArrayList<Double>()
        for (rowTag in rowTags) {
            // Split the row by <td> tags to get the columns
            val columnTags = rowTag.split(Regex("<td>|</td>")).filter { it.trim().isNotEmpty() }

            columnTags.forEach {
                try {
                    val v = it.toDouble()
                    values.add(v)
                    if (values.size == 6)
                        return values
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return values
    }
}