package uz.mahmudxon.currency.repo.cbu

import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.repo.VMResponse

interface ICbuRepo {
    suspend fun getCurrency(currency: String = "", date: String = ""): VMResponse<List<Currency>>
}