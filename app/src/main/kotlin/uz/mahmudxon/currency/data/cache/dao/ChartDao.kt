package uz.mahmudxon.currency.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.mahmudxon.currency.data.cache.tables.ChartTable

@Dao
interface ChartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(charts: List<ChartTable>)

    @Query("SELECT * FROM chart WHERE code = :code")
    suspend fun get(code: String): List<ChartTable>

    @Query("DELETE FROM chart WHERE code = :code")
    suspend fun delete(code: String)
}