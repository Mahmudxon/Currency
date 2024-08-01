package uz.mahmudxon.currency.data.network.tengebank

import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.util.addAll

class TengeBank(networkClient: NetworkClient) : CommercialBank(networkClient) {
    override val bank: Bank
        get() = Bank(
            id = Bank.Id.TengeBank.ordinal,
            name = "Tenge Bank",
            logo = "https://tengebank.uz/themes/halyk/assets/apple-touch-icon.png",
            website = "https://tengebank.uz/"
        )
    override val url: String
        get() = "https://tengebank.uz/uz/exchange-rates"

    override suspend fun getCurrencyPrice(responseAsText: String): List<Double> {
        val section =
            responseAsText.substringAfter("<section class=\"section section__exchange_rates\">")
                .substringBefore("</section>")
        val currencyValues: MutableList<Double> = mutableListOf()
        val regex =
            """<div class="currency__value (?:up|down)">(\d+(\.\d+)?) so'm</div>""".toRegex()

        regex.findAll(section).forEach { matchResult ->
            val valueText = matchResult.groupValues[1]
            val value = valueText.toDoubleOrNull()
            if (value != null) {
                currencyValues.add(value)
            }
        }
        val filteredValues = ArrayList<Double>()
        filteredValues.addAll(
            currencyValues[0],
            currencyValues[1],
            currencyValues[2],
            currencyValues[3],
            currencyValues[currencyValues.size - 2],
            currencyValues[currencyValues.size - 1]
        )
        return filteredValues
    }
}