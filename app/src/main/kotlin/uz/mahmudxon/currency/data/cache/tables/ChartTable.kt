package uz.mahmudxon.currency.data.cache.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chart",
    indices = [Index(value = ["code", "date"], unique = true)]
)
data class ChartTable(
    var code: String = "",
    var date: String = "",
    var rate: Double = 0.0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}