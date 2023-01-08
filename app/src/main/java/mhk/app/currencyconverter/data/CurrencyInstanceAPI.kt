package com.example.anime.data.data_source.dto

import mhk.app.currencyconverter.common.Constants
import mhk.app.currencyconverter.data.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyDTO
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyInstanceAPI {

    @GET("symbols")
    suspend fun getCurrencies() : Response<CurrencyDTO>

    @GET("latest")
    suspend fun getBaseCurrency(@Query("base") base: String) : Response<BaseCurrencyDTO>

}