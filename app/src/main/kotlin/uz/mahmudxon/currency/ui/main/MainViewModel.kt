package uz.mahmudxon.currency.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.mahmudxon.currency.model.Currency
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun selectCurrency(currency: Currency) {
        _state.value = _state.value.copy(selectedCurrency = currency)
    }

    fun deselectCurrency() {
        _state.value = _state.value.copy(selectedCurrency = null)
    }
}