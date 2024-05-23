package uz.mahmudxon.currency.data.network.cbu

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.mahmudxon.currency.model.Currency

@Serializable
data class CbuCurrency(
    @SerialName("Ccy")
    val ccy: String,
    @SerialName("CcyNm_EN")
    val ccyNmEN: String,
    @SerialName("CcyNm_RU")
    val ccyNmRU: String,
    @SerialName("CcyNm_UZ")
    val ccyNmUZ: String,
    @SerialName("CcyNm_UZC")
    val ccyNmUZC: String,
    @SerialName("Code")
    val code: String,
    @SerialName("Date")
    val date: String,
    @SerialName("Diff")
    val diff: String,
    @SerialName("id")
    val id: Int,
    @SerialName("Nominal")
    val nominal: String,
    @SerialName("Rate")
    val rate: String
) {
    fun toCurrency() = Currency(
        code = ccy,
        name = ccyNmUZ,
        rate = rate.toDouble(),
        date = date,
        diff = if (diff == "0" || diff.startsWith("-")) diff else "+$diff"
    )
}