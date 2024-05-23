package uz.mahmudxon.currency.data.network.cbu

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CbuChart (
    @SerialName("date")
    val date: String,
    @SerialName("value")
    val value: String
)