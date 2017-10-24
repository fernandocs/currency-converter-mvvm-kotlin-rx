package fernandocs.currencyconverter

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import fernandocs.currencyconverter.repository.CurrencyRepository
import fernandocs.currencyconverter.repository.api.FixerApi
import fernandocs.currencyconverter.viewmodel.CurrencyViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        val baseCurrency = "USD"
        val baseUrl = "http://api.fixer.io/"

        val SP_DEFAULT = "SP_DEFAULT"
        val SP_BASE_CURRENCY = "SP_BASE_CURRENCY"

        private lateinit var retrofit: Retrofit
        private lateinit var fixerApi: FixerApi
        private lateinit var currencyRepository: CurrencyRepository
        private lateinit var currencyViewModel: CurrencyViewModel

        fun injectFixerApi() = fixerApi
        fun injectCurrencyViewModel() = currencyViewModel

        fun getBaseCurrency(context: Context) =
                context.getSharedPreferences(App.SP_DEFAULT, Context.MODE_PRIVATE)
                        .getString(App.SP_BASE_CURRENCY, App.baseCurrency)!!

        fun setBaseCurrency(context: Context, newBaseCurrency: String) {
            context.getSharedPreferences(App.SP_DEFAULT, Context.MODE_PRIVATE)
                    .edit().putString(App.SP_BASE_CURRENCY, newBaseCurrency).apply()
        }
    }

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor()
        logging.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(httpClient.build())
                .build()

        fixerApi = retrofit.create(FixerApi::class.java)
        currencyRepository = CurrencyRepository(fixerApi)
        currencyViewModel = CurrencyViewModel(currencyRepository)
    }

}