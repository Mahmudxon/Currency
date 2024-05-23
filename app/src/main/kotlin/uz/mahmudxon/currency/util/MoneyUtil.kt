package uz.mahmudxon.currency.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.toMoneyString(): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    val formatter = if (this % 1 == 0.0) {
        DecimalFormat("#,##0", symbols)
    } else {
        DecimalFormat("#,##0.00", symbols)
    }
    return formatter.format(this)
}

fun String.moneyStringToDouble(): Double {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    try {
        val formatter = DecimalFormat("#,##0.00", symbols)
        return formatter.parse(this)?.toDouble() ?: 0.0
    } catch (e: Exception) {
        return 0.0
    }
}