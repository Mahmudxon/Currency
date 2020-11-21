package uz.mahmudxon.currency.ui.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.core.inject
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.repo.VMResponse
import uz.mahmudxon.currency.repo.cbu.ICbuRepo
import uz.mahmudxon.currency.ui.base.BaseViewModel
import uz.mahmudxon.currency.util.Event
import uz.mahmudxon.currency.util.TAG

class ConverterViewModel : BaseViewModel() {
    private val repo: ICbuRepo by inject()
    private val data = MutableLiveData<Currency>()
    private val error = MutableLiveData<Event<Exception>>()
    private val todayData = MutableLiveData<Currency>()
    private var c = ""
    private val today = DateTime()
    private var n = 1
    val loading = MutableLiveData<Boolean>()

    fun getToday(): LiveData<Currency> = todayData
    fun getData(): LiveData<Currency> = data
    fun getError(): LiveData<Event<Exception>> = error

    fun getData(c: String) {
        loading.value = true
        this.c = c
        val date = "%04d-%02d-%02d".format(today.yearOfEra, today.monthOfYear, today.dayOfMonth)
        Log.d(TAG, "getData: $date")
        viewModelScope.launch {
            when (val result = repo.getCurrency(c, date)) {
                is VMResponse.Success -> {
                    result.data.forEach { todayData.value = it }; prevDays()
                }
                is VMResponse.Error -> error.value = Event(result.exception)
            }
            loading.value = false
        }
    }

    private fun prevDays() {
        val date = today.minusDays(n)
        val s = "%04d-%02d-%02d".format(date.yearOfEra, date.monthOfYear, date.dayOfMonth)
        Log.d(TAG, "prevDays: $s")
        viewModelScope.launch {
            when (val result = repo.getCurrency(c, s)) {
                is VMResponse.Success -> {
                    result.data.forEach { data.value = it }; n++
                }
            }
            if (n < 180)
                prevDays()
        }
    }
}