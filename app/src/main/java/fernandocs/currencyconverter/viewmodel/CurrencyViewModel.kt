package fernandocs.currencyconverter.viewmodel

import fernandocs.currencyconverter.App
import fernandocs.currencyconverter.repository.CurrencyRepository

class CurrencyViewModel(private val currencyRepository: CurrencyRepository) {

    fun getRates(base : String = App.baseCurrency) = currencyRepository.getRates(base)
}