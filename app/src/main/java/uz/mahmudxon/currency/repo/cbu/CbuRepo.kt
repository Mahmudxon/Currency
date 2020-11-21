package uz.mahmudxon.currency.repo.cbu

import android.accounts.NetworkErrorException
import uz.mahmudxon.currency.api.cbu.ICbuService
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.repo.VMResponse

class CbuRepo(private val service: ICbuService) : ICbuRepo {
    override suspend fun getCurrency(currency: String, date: String): VMResponse<List<Currency>> {
        try {
            val result = service.getData(currency, date)
            if (result.isSuccessful && result.body() != null) {
                return VMResponse.Success(result.body()!!)
            }
            return VMResponse.Error(NetworkErrorException())
        } catch (e: Exception) {
            return VMResponse.Error(e)
        }
    }
}