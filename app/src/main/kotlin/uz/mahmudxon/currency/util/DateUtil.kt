package uz.mahmudxon.currency.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun Long.toShortDateString(): String {
    try {
        if (this < 0) return ""
        val format = SimpleDateFormat("dd.MM.yyyy")
        val date = Date(this)
        return format.format(date)
    } catch (e: Exception) {
        return ""
    }
}