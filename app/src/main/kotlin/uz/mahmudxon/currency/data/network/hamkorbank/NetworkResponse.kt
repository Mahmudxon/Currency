package uz.mahmudxon.currency.data.network.hamkorbank

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyData(
    val id: String,
    val destination_code: String,
    val currency_code: String,
    val currency_char: String,
    val selling_rate: Double,
    val buying_rate: Double,
    val sb_course: Double,
    val difference: Int,
    val created_at: String,
    val begin_date: String,
    val begin_sum_i: Int
)

@Serializable
data class Response(
    val status: String,
    val error_code: Int,
    val error_note: String,
    val data: List<CurrencyData>
)