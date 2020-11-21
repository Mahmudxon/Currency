package uz.mahmudxon.currency.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.repo.VMResponse
import uz.mahmudxon.currency.repo.cbu.ICbuRepo
import uz.mahmudxon.currency.ui.base.BaseViewModel
import uz.mahmudxon.currency.util.Event

class MainViewModel : BaseViewModel() {
    private val error = MutableLiveData<Event<Exception>>()
    private val data = MutableLiveData<List<Currency>>()
    private val repo: ICbuRepo by inject()
    val loading = MutableLiveData<Boolean>()
    fun getData(): LiveData<List<Currency>> = data
    fun getError(): LiveData<Event<Exception>> = error

    fun requestDataFromCBU(date: String = "", currency: String = "all") {
        loading.value = true
        viewModelScope.launch {
            when (val result = repo.getCurrency(currency, date)) {
                is VMResponse.Success -> data.value = result.data
                is VMResponse.Error -> error.value = Event(result.exception)
            }
            loading.value = false
        }
    }
}