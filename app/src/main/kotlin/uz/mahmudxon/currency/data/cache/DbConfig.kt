package uz.mahmudxon.currency.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mahmudxon.currency.data.cache.dao.ChartDao
import uz.mahmudxon.currency.data.cache.dao.CommercialPriceDao
import uz.mahmudxon.currency.data.cache.dao.CurrencyDao
import uz.mahmudxon.currency.data.cache.tables.ChartTable
import uz.mahmudxon.currency.data.cache.tables.CommercialPriceTable
import uz.mahmudxon.currency.data.cache.tables.CurrencyTable

@Database(
    entities = [CurrencyTable::class, ChartTable::class, CommercialPriceTable::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDb : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun chartDao(): ChartDao
    abstract fun commercialPriceDao(): CommercialPriceDao
}