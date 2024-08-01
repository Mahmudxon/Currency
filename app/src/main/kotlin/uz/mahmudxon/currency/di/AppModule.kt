package uz.mahmudxon.currency.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import uz.mahmudxon.currency.data.cache.CurrencyDb
import uz.mahmudxon.currency.data.cache.dao.ChartDao
import uz.mahmudxon.currency.data.cache.dao.CommercialPriceDao
import uz.mahmudxon.currency.data.cache.dao.CurrencyDao
import uz.mahmudxon.currency.data.cache.mapper.CommercialPriceMapper
import uz.mahmudxon.currency.data.cache.mapper.CurrencyMapper
import uz.mahmudxon.currency.data.cache.prefs.Prefs
import uz.mahmudxon.currency.data.network.NetworkClient
import uz.mahmudxon.currency.data.network.agrobank.AgroBank
import uz.mahmudxon.currency.data.network.aloqabank.AloqaBank
import uz.mahmudxon.currency.data.network.cbu.Cbu
import uz.mahmudxon.currency.data.network.commercial.CommercialBank
import uz.mahmudxon.currency.data.network.davrbank.DavrBank
import uz.mahmudxon.currency.data.network.hamkorbank.HamkorBank
import uz.mahmudxon.currency.data.network.infinbank.InfinBank
import uz.mahmudxon.currency.data.network.ipotekabank.IpotekaBank
import uz.mahmudxon.currency.data.network.kapital.KapitalBank
import uz.mahmudxon.currency.data.network.mk.MkBank
import uz.mahmudxon.currency.data.network.nbu.Nbu
import uz.mahmudxon.currency.data.network.ofb.Ofb
import uz.mahmudxon.currency.data.network.sqb.Sqb
import uz.mahmudxon.currency.data.network.tengebank.TengeBank
import uz.mahmudxon.currency.data.network.turonbank.TuronBank
import uz.mahmudxon.currency.data.network.xb.XalqBanki
import uz.mahmudxon.currency.data.repo.GetCommercialBankData
import uz.mahmudxon.currency.data.repo.GetCurrencyChart
import uz.mahmudxon.currency.data.repo.GetCurrencyList

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNetworkClient(@ApplicationContext context: Context): NetworkClient =
        NetworkClient(context)

    @Provides
    fun provideCbu(networkClient: NetworkClient): Cbu = Cbu(networkClient)

    @Provides
    fun provideDbConfig(@ApplicationContext context: Context): CurrencyDb =
        Room.databaseBuilder(context, CurrencyDb::class.java, "currency.db")
            .allowMainThreadQueries()
            .build()

    @Provides
    fun providePrefs(@ApplicationContext context: Context): Prefs = Prefs(context)

    @Provides
    fun provideGetCbuCurrencyList(cbu: Cbu, dao: CurrencyDao, prefs: Prefs): GetCurrencyList =
        GetCurrencyList(
            cbu = cbu,
            dao = dao,
            mapper = CurrencyMapper(),
            prefs = prefs
        )

    @Provides
    fun provideGetCbuCurrencyChart(cbu: Cbu, dao: ChartDao, prefs: Prefs): GetCurrencyChart =
        GetCurrencyChart(
            cbu = cbu, dao = dao, prefs = prefs
        )

    @Provides
    fun provideCurrencyDao(db: CurrencyDb): CurrencyDao = db.currencyDao()

    @Provides
    fun provideChartDao(db: CurrencyDb): ChartDao = db.chartDao()

    @Provides
    fun provideCommercialPriceDao(db: CurrencyDb): CommercialPriceDao = db.commercialPriceDao()

    @Provides
    fun provideGetBankData(
        commercialBanks: Set<@JvmSuppressWildcards CommercialBank>,
        dao: CommercialPriceDao,
        prefs: Prefs
    ): GetCommercialBankData =
        GetCommercialBankData(
            banks = commercialBanks.toList(),
            dao = dao,
            mapper = CommercialPriceMapper(),
            prefs = prefs
        )

    @Provides
    @IntoSet
    fun provideNbu(networkClient: NetworkClient): CommercialBank = Nbu(networkClient)

    @Provides
    @IntoSet
    fun provideKapitalBank(networkClient: NetworkClient): CommercialBank =
        KapitalBank(networkClient)

    @Provides
    @IntoSet
    fun provideAgroBank(networkClient: NetworkClient): CommercialBank =
        AgroBank(networkClient)

    @Provides
    @IntoSet
    fun provideTuronBank(networkClient: NetworkClient): CommercialBank = TuronBank(networkClient)

//    @Provides
//    @IntoSet
//    fun provideDavrBank(networkClient: NetworkClient): CommercialBank = DavrBank(networkClient)

    @Provides
    @IntoSet
    fun provideAloqaBank(networkClient: NetworkClient): CommercialBank = AloqaBank(networkClient)

    @Provides
    @IntoSet
    fun provideOfb(networkClient: NetworkClient): CommercialBank = Ofb(networkClient)

    @Provides
    @IntoSet
    fun provideMkBank(networkClient: NetworkClient): CommercialBank = MkBank(networkClient)

    @Provides
    @IntoSet
    fun provideSqb(networkClient: NetworkClient): CommercialBank = Sqb(networkClient)

    @Provides
    @IntoSet
    fun provideIpotekaBank(networkClient: NetworkClient): CommercialBank =
        IpotekaBank(networkClient)

    @Provides
    @IntoSet
    fun provideXalqBanki(networkClient: NetworkClient): CommercialBank = XalqBanki(networkClient)

    @Provides
    @IntoSet
    fun provideHammaBank(networkClient: NetworkClient): CommercialBank = HamkorBank(networkClient)

    @Provides
    @IntoSet
    fun provideInfinitBank(networkClient: NetworkClient): CommercialBank = InfinBank(networkClient)

    @Provides
    @IntoSet
    fun provideTengeBank(networkClient: NetworkClient): CommercialBank = TengeBank(networkClient)

}