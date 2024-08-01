package uz.mahmudxon.currency.data.network.kapital

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank

class KapitalBank(
    networkClient: NetworkClient
) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.KAPITAL.ordinal,
            name = "Kapital Bank",
            website = "https://kapitalbank.uz/",
            logo = "https://www.kapitalbank.uz/favicon.ico?v2",
            address = "100047, Toshkent shahri, Yunusobod tumani, Sayilgox ko‘chasi, 7",
            phone = "200-15-15",
            licens = "69, 07.04.2001",
            inn = "203591761"
        )
    override val url: String
        get() = "https://www.kapitalbank.uz/uz/services/exchange-rates/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val values = ArrayList<Double>()
        val usd = responseAsText.substringAfter("<tbody id=\"USD\"")
            .substringBefore("Markaziy amaliyot bo’limi")
        val eur = responseAsText.substringAfter("<tbody id=\"EUR\"")
            .substringBefore("Markaziy amaliyot bo’limi")
        val rub = responseAsText.substringAfter("<tbody id=\"RUB\"")
            .substringBefore("Markaziy amaliyot bo’limi")

        usd.split(Regex("<p align=\"center\">|</p>")).forEach {
            try {
                val v = it.toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        eur.split(Regex("<p align=\"center\">|</p>")).forEach {
            try {
                val v = it.toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        rub.split(Regex("<p align=\"center\">|</p>")).forEach {
            try {
                val v = it.toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return values
    }
}