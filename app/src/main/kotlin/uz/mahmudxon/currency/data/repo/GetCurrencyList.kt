package uz.mahmudxon.currency.data.repo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.data.cache.dao.CurrencyDao
import uz.mahmudxon.currency.data.cache.mapper.CurrencyMapper
import uz.mahmudxon.currency.data.cache.prefs.PrefKeys
import uz.mahmudxon.currency.data.cache.prefs.Prefs
import uz.mahmudxon.currency.data.network.cbu.Cbu
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.util.DataState
import uz.mahmudxon.currency.util.dlog
import javax.inject.Inject

class GetCurrencyList @Inject constructor(
    private val cbu: Cbu,
    private val dao: CurrencyDao,
    private val prefs: Prefs,
    @ApplicationContext private val ctx: Context // Bad Idea
) {
    // I know sh**t!!! But I have no idea to fix search currency on view model
    // I'll fix it later when I have time
    val codes by lazy { ctx.resources.getStringArray(R.array.currency_codes) }
    val names by lazy { ctx.resources.getStringArray(R.array.currency_names) }
    val currencyNameMap by lazy { codes.zip(names).toMap() }
    private val mapper = CurrencyMapper()
    fun execute(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.loading())

        val lastUpdate = prefs.get(PrefKeys.currencyUpdate, 0L)

        // 6 hour
        val isLocalDataActual = System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000

        if (isLocalDataActual) {
            val response = dao.getAll().map {
                // Duplicate
                it.name = currencyNameMap[it.code] ?: it.name
                mapper.mapToDomain(it)
            }
            emit(DataState.data(response))
        } else {
            val response = cbu.getCurrencyList()
                .map {
                    // OMG! Bug! Because it write database in locale language
                    val currency = it.toCurrency()
                    val currencyNameLocale = currencyNameMap[currency.code]
                    if (currencyNameLocale != null)
                        currency.copy(
                            name = currencyNameLocale
                        ) else currency
                }
            dao.deleteAll()
            dao.upsert(response.map {
                mapper.mapFromDomain(it)
            })
            prefs.save(PrefKeys.currencyUpdate, System.currentTimeMillis())
            emit(DataState.data(response))
        }
    }.catch {
        dlog(it)
        emit(DataState.error(Exception(it)))
    }
}