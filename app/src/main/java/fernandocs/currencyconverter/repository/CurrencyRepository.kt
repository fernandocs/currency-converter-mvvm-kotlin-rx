package fernandocs.currencyconverter.repository

import fernandocs.currencyconverter.model.Rate
import fernandocs.currencyconverter.repository.api.FixerApi
import io.reactivex.Observable

class CurrencyRepository(private val fixerApi: FixerApi) {

    fun getRates(base : String) = fixerApi.getRates(base)
            .flatMap { Observable.fromIterable(it.rates.entries) }
            .map { Rate(it.key, it.value) }!!

}