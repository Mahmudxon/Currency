package uz.mahmudxon.currency.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.mahmudxon.currency.data.cache.dao.ChartDao
import uz.mahmudxon.currency.data.cache.mapper.ChartMapper
import uz.mahmudxon.currency.data.cache.prefs.PrefKeys
import uz.mahmudxon.currency.data.cache.prefs.Prefs
import uz.mahmudxon.currency.data.network.cbu.Cbu
import uz.mahmudxon.currency.model.Chart
import uz.mahmudxon.currency.util.DataState

class GetCurrencyChart(private val cbu: Cbu, private val dao: ChartDao, private val prefs: Prefs) {
    fun execute(code: String): Flow<DataState<List<Chart>>> = flow<DataState<List<Chart>>> {
        emit(DataState.loading())
        val mapper = ChartMapper(code)
        val lastUpdate = prefs.get(PrefKeys.chartUpdate + code, 0L)
        // 6 hour
        val isLocalDataActual = System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000
        if (isLocalDataActual) {
            val localData = dao.get(code).map { mapper.mapToDomain(it) }
            emit(DataState.data(localData))
        } else {
            val response =
                cbu.getCurrencyChart(code).map {
                    Chart(
                        date = it.date,
                        rate = it.value.toDouble()
                    )
                }

            dao.delete(code)
            dao.insert(response.map { mapper.mapFromDomain(it) })
            prefs.save(PrefKeys.chartUpdate + code, System.currentTimeMillis())
            emit(DataState.data(response))
        }
    }.catch {
        emit(DataState.error(Exception(it)))
    }
}