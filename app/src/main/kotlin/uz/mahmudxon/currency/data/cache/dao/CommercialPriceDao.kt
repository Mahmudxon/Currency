package uz.mahmudxon.currency.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.mahmudxon.currency.data.cache.tables.CommercialPriceTable

@Dao
interface CommercialPriceDao {

    @Insert
    suspend fun insert(prices: List<CommercialPriceTable>)

    @Query("SELECT * FROM commercial_price where currencyCode = :code")
    suspend fun getAll(code: String): List<CommercialPriceTable>

    @Query("DELETE FROM commercial_price")
    suspend fun deleteAll()
}