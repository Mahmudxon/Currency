package uz.mahmudxon.currency.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.mahmudxon.currency.data.cache.dao.CurrencyDao
import uz.mahmudxon.currency.data.cache.mapper.CurrencyMapper
import uz.mahmudxon.currency.data.cache.prefs.PrefKeys
import uz.mahmudxon.currency.data.cache.prefs.Prefs
import uz.mahmudxon.currency.data.network.cbu.Cbu
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.util.DataState
import uz.mahmudxon.currency.util.dlog

class GetCurrencyList(
    private val cbu: Cbu,
    private val dao: CurrencyDao,
    private val mapper: CurrencyMapper,
    private val prefs: Prefs
) {
    fun execute(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.loading())

        val lastUpdate = prefs.get(PrefKeys.currencyUpdate, 0L)

        // 6 hour
        val isLocalDataActual = System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000

        if (isLocalDataActual) {
            val response = dao.getAll().map { mapper.mapToDomain(it) }
            emit(DataState.data(response))
        } else {
            val response = cbu.getCurrencyList()
                .map { it.toCurrency() }
            dao.deleteAll()
            dao.upsert(response.map { mapper.mapFromDomain(it) })
            prefs.save(PrefKeys.currencyUpdate, System.currentTimeMillis())
            emit(DataState.data(response))
        }
    }.catch {
        dlog(it)
        emit(DataState.error(Exception(it)))
    }
}