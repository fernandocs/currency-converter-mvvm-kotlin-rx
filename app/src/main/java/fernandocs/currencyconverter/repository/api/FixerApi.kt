package fernandocs.currencyconverter.repository.api

import fernandocs.currencyconverter.model.Result
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {
    @GET("latest")
    fun getRates(@Query("base") base : String) : Observable<Result>
}