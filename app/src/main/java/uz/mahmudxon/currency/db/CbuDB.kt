package uz.mahmudxon.currency.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mahmudxon.currency.db.dao.currency.ICurrencyDao
import uz.mahmudxon.currency.model.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class CbuDB : RoomDatabase() {
    abstract fun currencyDao() : ICurrencyDao
}