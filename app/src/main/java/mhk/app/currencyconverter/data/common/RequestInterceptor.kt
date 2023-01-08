package mhk.app.currencyconverter.data.common

import mhk.app.currencyconverter.common.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request().newBuilder()
            .addHeader("apikey", Constants.API_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}

//https://api.apilayer.com/symbols