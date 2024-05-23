package uz.mahmudxon.currency.ui.currencyList

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.mahmudxon.currency.data.repo.GetCurrencyList
import uz.mahmudxon.currency.model.Currency
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewmodel @Inject constructor(
    private val getCurrencyList: GetCurrencyList
) : ViewModel() {
    private val _state = MutableStateFlow(CurrencyListState())
    val state = _state.asStateFlow()
    private val currencyList = ArrayList<Currency>()

    init {
        getCurrencyList()
    }

    private fun getCurrencyList() {
        getCurrencyList.execute()
            .onEach { dataState ->
                _state.value = _state.value.copy(
                    isLoading = dataState.isLoading
                )
                dataState.data?.let {
                    currencyList.clear()
                    currencyList.addAll(it)
                    filter()
                }
                _state.value = _state.value.copy(
                    errorCode = if (dataState.error == null) -1 else 1
                )
            }
            .launchIn(CoroutineScope(IO))
    }

    fun onEvent(event: CurrencyListEvent) {
        when (event) {
            is CurrencyListEvent.SelectCurrency -> {

            }

            is CurrencyListEvent.Search -> {
                _state.value = _state.value.copy(
                    searchQuery = event.query
                )
                filter()
            }

            is CurrencyListEvent.OnRefresh -> {
                getCurrencyList()
            }
        }
    }

    private fun filter() {
        val query = _state.value.searchQuery
        if (query.isEmpty()) {
            _state.value = _state.value.copy(
                currencies = currencyList
            )
            return
        }

        _state.value = _state.value.copy(
            currencies = currencyList.filter {
                it.code.contains(query, ignoreCase = true) || it.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        )
    }

}