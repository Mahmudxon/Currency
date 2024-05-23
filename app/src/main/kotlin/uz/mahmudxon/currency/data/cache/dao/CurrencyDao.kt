package uz.mahmudxon.currency.data.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import uz.mahmudxon.currency.data.cache.tables.CurrencyTable

@Dao
interface CurrencyDao {

    @Upsert
    suspend fun upsert(currencies: List<CurrencyTable>)

    @Query("SELECT * FROM currency")
    suspend fun getAll(): List<CurrencyTable>

    @Query("SELECT * FROM currency WHERE code = :code")
    suspend fun getCurrency(code: String): CurrencyTable

    @Delete
    suspend fun delete(currency: CurrencyTable)

    @Query("DELETE FROM currency")
    suspend fun deleteAll()
}