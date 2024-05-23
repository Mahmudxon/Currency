package uz.mahmudxon.currency.data.network.cbu

import uz.mahmudxon.currency.data.network.NetworkClient

class Cbu(private val networkClient: NetworkClient) {
    private val currencyListUrl = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/"
    private val currencyChartUrl = "https://cbu.uz/common/json/amcharts.php?rate="

    suspend fun getCurrencyList() = networkClient.get<List<CbuCurrency>>(currencyListUrl)
    suspend fun getCurrencyChart(code: String) =
        networkClient.get<List<CbuChart>>(currencyChartUrl + code)
}