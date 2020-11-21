package uz.mahmudxon.currency.db.dao.currency

import androidx.room.Dao
import uz.mahmudxon.currency.db.dao.base.IBaseDao
import uz.mahmudxon.currency.model.Currency

@Dao
interface ICurrencyDao : IBaseDao<Currency>{
}