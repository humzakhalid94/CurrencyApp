package com.example.anime.data.data_source.dto

import mhk.app.currencyconverter.common.Constants
import mhk.app.currencyconverter.data.data_source.dto.CurrencyDTO
import retrofit2.http.GET
import retrofit2.Response

interface CurrencyInstanceAPI {

    @GET("symbols")
    suspend fun getCurrencies() : Response<CurrencyDTO>

}