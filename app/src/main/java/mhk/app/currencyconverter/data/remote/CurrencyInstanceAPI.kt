package mhk.app.currencyconverter.data.remote

import mhk.app.currencyconverter.data.remote.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.data.remote.data_source.dto.CurrencyDTO
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface CurrencyInstanceAPI {

    @GET("symbols")
    suspend fun getCurrencies() : Response<CurrencyDTO>

    @GET("latest")
    suspend fun getBaseCurrency(@Query("base") base: String) : Response<BaseCurrencyDTO>

}