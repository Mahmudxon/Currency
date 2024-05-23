package uz.mahmudxon.currency.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json


class NetworkClient(val context: Context) {
    suspend inline fun <reified T> get(url: String): T {
        HttpClient(OkHttp) {
            install(ContentNegotiation)
            {
                json()
            }
            engine {
                addInterceptor(ChuckerInterceptor(context))
            }
        }.use { client ->
            val data: T = client.get(url).body()
            return data
        }
    }

    suspend fun getHtml(url: String): HttpResponse {
        HttpClient(OkHttp) {
            headers {
                append("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:126.0) Gecko/20100101 Firefox/126.0")
            }
            engine {
                addInterceptor(ChuckerInterceptor(context))
            }
        }.use { client ->
            val data = client.get(url)
            return data
        }
    }
}