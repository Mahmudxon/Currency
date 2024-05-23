package uz.mahmudxon.currency.data.network.commercial

import io.ktor.client.statement.readBytes
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.model.BankPrice
import uz.mahmudxon.currency.util.toShortDateString

abstract class CommercialBank(
    private val networkClient: NetworkClient
) {
    abstract val bank: Bank
    protected abstract val url: String
    protected abstract suspend fun getCurrencyPrice(responseAsText: String): List<Double>

    suspend fun getBankPrice(): List<BankPrice> {
        val response = networkClient.getHtml(url).readBytes()
        val responseAsText = response.decodeToString()

        val currList = getCurrencyPrice(responseAsText)
        val date = System.currentTimeMillis().toShortDateString()
        val dollar = BankPrice(
            bank = bank,
            currencyCode = "USD",
            date = date,
            buy = currList[0],
            sell = currList[1]
        )

        val euro = BankPrice(
            bank = bank,
            currencyCode = "EUR",
            date = date,
            buy = currList[2],
            sell = currList[3]
        )
        if (currList.size < 6)
            return listOf(dollar, euro)
        val ruble = BankPrice(
            bank = bank,
            currencyCode = "RUB",
            date = date,
            buy = currList[4],
            sell = currList[5]
        )
        return listOf(dollar, euro, ruble)
    }
}