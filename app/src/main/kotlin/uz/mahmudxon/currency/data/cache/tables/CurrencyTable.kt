package uz.mahmudxon.currency.data.cache.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("currency")
data class CurrencyTable(
    var code: String = "",
    var name: String = "",
    var rate: Double = 0.0,
    var date: String = "",
    var diff: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}