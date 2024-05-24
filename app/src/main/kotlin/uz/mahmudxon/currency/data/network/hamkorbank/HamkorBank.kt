package uz.mahmudxon.currency.data.network.hamkorbank

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll


class HamkorBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.HamkorBank.ordinal,
            name = "Hamkor Bank",
            website = "https://www.hamkorbank.uz/",
            logo = "file:///android_asset/bank/hamkorbank.webp"
        )
    override val url: String
        get() = "https://hamkorbank.uz/uz/exchange-rate/"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val values = ArrayList<Double>()
//        val xpath = XPathFactory.newInstance().newXPath()
//        val address =
//            "/html/body/div[2]/div[2]/div/div[2]/div/div/div[2]/div/div/div/div/div/div[2]/div/ul[2]/li[4]/span"
//        val value = xpath.evaluate(address, InputSource(responseAsText.byteInputStream()))
//        dlog(value)

        val div = responseAsText.substringAfter("<div class=\"exchangeRates__content\">")
            .substringBefore("<div class=\"mainSection6-overlay\">")
        val spanTags = div.split(Regex("<span>|</span>")).filter { it.trim().isNotEmpty() }
        for (spanTag in spanTags) {
            try {
                val v = spanTag.replace(" ", "").replace(",", ".").toDouble()
                values.add(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // [12712.88, 12670.0, 12770.0, 12712.88, 12670.0, 12770.0, 13788.39, 13100.0, 14000.0, 13788.39, 13100.0, 14000.0, 140.85, 120.0, 141.0, 140.85, 120.0, 141.0, 16177.14, 15400.0, 17000.0, 16177.14, 15400.0, 17000.0, 81.15, 77.0, 85.0, 81.15, 77.0, 85.0, 13913.63, 13200.0, 14600.0, 13913.63, 13200.0, 14600.0]
        // [12670, 12770, 13100, 14000, 120, 141]
        // 1, 2, 7, 8, 13, 14

        val filteredValues = ArrayList<Double>()
        filteredValues.addAll(
            values[1], values[2], values[7], values[8], values[13], values[14]
        )
        return filteredValues
    }
}