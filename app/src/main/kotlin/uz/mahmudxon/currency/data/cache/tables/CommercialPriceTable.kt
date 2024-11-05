package uz.mahmudxon.currency.data.cache.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "commercial_price",
    indices = [Index(value = ["bankId", "currencyCode"], unique = true)]
)
data class CommercialPriceTable(
    val currencyCode: String,
    val date: String,
    val sell: Double,
    val buy: Double,
    val bankId: Int,
    val bankName: String,
    val bankLogo: String,
    val bankWebsite: String,
    val bankAddress: String,
    val bankPhone: String,
    val bankLicens: String,
    val bankInn: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}