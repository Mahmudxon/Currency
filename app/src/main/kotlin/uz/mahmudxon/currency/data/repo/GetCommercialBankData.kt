package uz.mahmudxon.currency.data.repo

import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.mahmudxon.currency.data.cache.dao.CommercialPriceDao
import uz.mahmudxon.currency.data.cache.mapper.CommercialPriceMapper
import uz.mahmudxon.currency.data.cache.prefs.PrefKeys
import uz.mahmudxon.currency.data.cache.prefs.Prefs
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.model.BankPrice
import uz.mahmudxon.currency.util.DataState
import uz.mahmudxon.currency.util.asyncAll

class GetCommercialBankData(
    private val banks: List<CommercialBank>,
    private val dao: CommercialPriceDao,
    private val mapper: CommercialPriceMapper,
    private val prefs: Prefs
) {
    fun execute(code: String): Flow<DataState<List<BankPrice>>> = flow<DataState<List<BankPrice>>> {
        emit(DataState.loading())

        val lastUpdate = prefs.get(PrefKeys.priceUpdate, 0L)

        // 6 hour
        val isLocalDataActual =
            System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000 // && !BuildConfig.DEBUG
        if (isLocalDataActual) {
            val response = dao.getAll(code).map { mapper.mapToDomain(it) }
            emit(DataState.data(response))
        } else {
            val prices = collectBankAsync()
            if (prices.isNotEmpty()) {
                dao.deleteAll()
                dao.insert(prices.map { mapper.mapFromDomain(it) })
                prefs.save(PrefKeys.priceUpdate, System.currentTimeMillis())
                emit(DataState.data(prices.filter { it.currencyCode == code }))
            }
        }
    }.catch {
        emit(DataState.error(Exception(it)))
    }

    private suspend fun collectBankAsync() = coroutineScope {
        asyncAll(banks) {
            try {
                it.getBankPrice()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }.awaitAll().reduce { acc, list ->
            acc + list
        }
    }


}