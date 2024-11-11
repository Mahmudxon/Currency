package uz.mahmudxon.currency.ui.currencyDetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.mahmudxon.currency.data.repo.GetCommercialBankData
import uz.mahmudxon.currency.data.repo.GetCurrencyChart
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.model.BankPrice
import uz.mahmudxon.currency.model.BestOffer
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.util.dlog
import uz.mahmudxon.currency.util.moneyStringToDouble
import uz.mahmudxon.currency.util.toMoneyString
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailsViewModel @Inject constructor(
    private val getCurrencyChart: GetCurrencyChart,
    private val getCommercialBankData: GetCommercialBankData
) : ViewModel() {
    private val _state = MutableStateFlow(CurrencyDetailsState())
    val state = _state.asStateFlow()

    fun getCurrency(currency: Currency) {
        if (_state.value.currency?.code == currency.code) return
        _state.value = _state.value.copy(
            currency = currency,
            localValue = currency.rate.toMoneyString(),
            foreignValue = "1",
            chart = emptyList(),
            bankPrices = emptyList(),
            bestOfferForSelling = BestOffer(),
            bestOfferForBuying = BestOffer(),
            showBankPrice = null,
            sellingAvailable = false
        )
        getData()
    }

    private fun getData() {
        getCurrencyChart.execute(_state.value.currency!!.code)
            .onEach {
                _state.value = _state.value.copy(isLoading = it.isLoading)
                it.data?.let { data ->
                    val step = if (data.size > 300) data.size / 300 else 1
                    _state.value = _state.value.copy(
                        chart = data.filterIndexed { index, _ ->
                            if (index == data.size - 1) true // last item
                            else index % step == 0
                        }
                    )
                    dlog(_state.value.chart)
                }
                _state.value = _state.value.copy(
                    error = if (it.error == null) -1 else 1
                )
            }
            .launchIn(CoroutineScope(IO))
        if (_state.value.currency?.code == "USD" || _state.value.currency?.code == "EUR" || _state.value.currency?.code == "RUB") {
            _state.value = _state.value.copy(sellingAvailable = true)
            getCommercialBankData.execute(_state.value.currency!!.code)
                .onEach {
                    _state.value = _state.value.copy(
                        sellingLoading = it.isLoading
                    )
                    it.data?.let {
                        val prices = it
                            .sortedBy { it.bank.name }
                        _state.value = _state.value.copy(
                            bankPrices = prices
                        )
                        findBestOffer(prices)
                    }
                }
                .launchIn(CoroutineScope(IO))
        } else {
            _state.value = _state.value.copy(sellingAvailable = false)
        }
    }


    private fun findBestOffer(prices: List<BankPrice>) {
        var buy = Double.MIN_VALUE
        var sell = Double.MAX_VALUE
        val buyBanks = ArrayList<Bank>()
        val sellBanks = ArrayList<Bank>()

        prices.forEach {
            if (it.sell == sell) {
                sellBanks.add(it.bank)
            }

            if (it.sell > 0 && it.sell < sell) {
                sellBanks.clear()
                sellBanks.add(it.bank)
                sell = it.sell
            }

            if (it.buy == buy) {
                buyBanks.add(it.bank)
            }

            if (it.buy > 0 && it.buy > buy) {
                buyBanks.clear()
                buyBanks.add(it.bank)
                buy = it.buy
            }
        }

        _state.value = _state.value.copy(
            bestOfferForSelling = BestOffer(
                price = sell,
                banks = sellBanks
            ),

            bestOfferForBuying = BestOffer(
                price = buy,
                banks = buyBanks
            )
        )
    }

    fun onEvent(event: CurrencyDetailsEvent) {
        when (event) {
            is CurrencyDetailsEvent.Refresh -> {
                getData()
            }

            is CurrencyDetailsEvent.SwitchCurrency -> {
                _state.value = _state.value.copy(
                    isForeignCurrencyTop = !_state.value.isForeignCurrencyTop
                )
            }

            is CurrencyDetailsEvent.ForeignCurrencyInput -> {
                val d = _state.value.currency?.rate ?: 1.0
                val v = event.value.moneyStringToDouble()
                val localValue = v * d
                _state.value = _state.value.copy(
                    foreignValue = event.value,
                    localValue = localValue.toMoneyString()
                )
            }

            is CurrencyDetailsEvent.LocalCurrencyInput -> {
                val d = _state.value.currency?.rate ?: 1.0
                val v = event.value.moneyStringToDouble()
                val foreignValue = v / d
                _state.value = _state.value.copy(
                    localValue = event.value,
                    foreignValue = foreignValue.toMoneyString()
                )
            }

            is CurrencyDetailsEvent.CurrencyPriceClick -> {
                _state.value = _state.value.copy(
                    showBankPrice = event.price
                )
            }

            CurrencyDetailsEvent.OnCurrencyInfoDismissRequest -> {
                _state.value = _state.value.copy(
                    showBankPrice = null
                )
            }
        }
    }
}