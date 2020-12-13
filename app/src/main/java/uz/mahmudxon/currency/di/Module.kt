package uz.mahmudxon.currency.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.mahmudxon.currency.BuildConfig
import uz.mahmudxon.currency.api.cbu.ICbuService
import uz.mahmudxon.currency.db.CbuDB
import uz.mahmudxon.currency.db.dao.currency.ICurrencyDao
import uz.mahmudxon.currency.list.currency.CurrencyAdapter
import uz.mahmudxon.currency.list.history.HistoryAdapter
import uz.mahmudxon.currency.repo.cbu.CbuRepo
import uz.mahmudxon.currency.repo.cbu.ICbuRepo
import uz.mahmudxon.currency.ui.converter.ConverterFragment
import uz.mahmudxon.currency.ui.converter.ConverterViewModel
import uz.mahmudxon.currency.ui.feedback.FeedbackFragment
import uz.mahmudxon.currency.ui.main.MainFragment
import uz.mahmudxon.currency.ui.main.MainViewModel
import uz.mahmudxon.currency.util.Constants
import uz.mahmudxon.currency.util.TAG
import java.util.concurrent.TimeUnit


val networkModule = module {
    single { ChuckInterceptor(get()) }
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor { message -> Log.d(TAG, message) }
    }
    single {
        val builder = OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
        if (BuildConfig.DEBUG)
            builder.addInterceptor(get<ChuckInterceptor>())
                .addNetworkInterceptor(get<HttpLoggingInterceptor>())
        return@single builder.build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}

val dbModule = module {
    single<CbuDB> { Room.databaseBuilder(get(), CbuDB::class.java, "music.mp3").build() }
    single<ICurrencyDao> { get<CbuDB>().currencyDao() }
}

val cbuModule = module {
    single<ICbuService> { get<Retrofit>().create(ICbuService::class.java) }
    single<ICbuRepo> { CbuRepo(get()) }
}

val mainModule = module {
    scope(named<MainFragment>()) {
        viewModel { MainViewModel() }
        factory { CurrencyAdapter() }
    }
}

val converterModule = module {
    scope(named<ConverterFragment>()) {
        viewModel { ConverterViewModel() }
        factory { HistoryAdapter() }
    }
}

val feedbackModule = module {
    scope(named<FeedbackFragment>())
    {
        factory {
            BackgroundMail.newBuilder(get<FeedbackFragment>().context)
                .withUsername(Constants.GMailUsername)
                .withPassword(Constants.GmailPassword)
                .withMailTo(Constants.InboxMail)
        }
    }
}

val koinModules =
    listOf(networkModule, dbModule, cbuModule, mainModule, converterModule, feedbackModule)