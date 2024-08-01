package uz.mahmudxon.currency.data.network.turonbank

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class TuronBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.TURON.ordinal,
            name = "Turon Bank",
            website = "https://turonbank.uz/",
            logo = "https://turonbank.uz/favicons/apple-touch-icon.png",
            address = "100011, Toshkent shahri, Shayxontoxur tumani, Abay ko‘chasi, 4a",
            phone = "144-60-00",
            licens = "8, 20.09.1993",
            inn = "201055108"
        )
    override val url: String
        get() = "https://turonbank.uz/uz/services/exchange-rates/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val values = ArrayList<Double>()
        val currency = responseAsText.substringAfter(" <div class=\"exchange__main\">")
            .substringBefore("<div class=\"exchange__date\">")
        val spanTags = currency.split(Regex("<span>|</span>")).filter { it.trim().isNotEmpty() }
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