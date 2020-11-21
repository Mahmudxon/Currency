package uz.mahmudxon.currency.api.cbu

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import uz.mahmudxon.currency.model.Currency

interface ICbuService {
    @GET("arkhiv-kursov-valyut/json/{currency}/{date}/")
    suspend fun getData(
        @Path("currency") currency: String,
        @Path("date") date: String
    ): Response<List<Currency>>
}