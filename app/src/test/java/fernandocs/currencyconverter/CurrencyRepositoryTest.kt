package fernandocs.currencyconverter

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import fernandocs.currencyconverter.model.Result
import fernandocs.currencyconverter.repository.CurrencyRepository
import fernandocs.currencyconverter.repository.api.FixerApi
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class CurrencyRepositoryTest {

    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var fixerApi: FixerApi

    @Before
    fun setup() {
        fixerApi = mock()

        currencyRepository = CurrencyRepository(fixerApi)
    }

    @Test
    fun getRatesEmpty() {
        `when`(fixerApi.getRates(any())).thenReturn(Observable.just(getFixtureNoRates()))
        currencyRepository.getRates("USD").test()
    }

    @Test
    fun getRatesWithValues() {
        `when`(fixerApi.getRates(any())).thenReturn(Observable.just(getFixtureHasRates()))
        currencyRepository.getRates("USD").test()
    }

    private fun getFixtureNoRates() = Result("USD", "2017-10-23", emptyMap())
    private fun getFixtureHasRates() = Result("USD", "2017-10-23", mapOf(Pair("BRL", 3.1936)))
}
