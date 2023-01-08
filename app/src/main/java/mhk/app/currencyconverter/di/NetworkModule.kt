package mhk.app.currencyconverter.di

import android.content.Context
import com.example.anime.data.data_source.dto.CurrencyInstanceAPI
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mhk.app.currencyconverter.common.Constants
import mhk.app.currencyconverter.data.common.RequestInterceptor
import mhk.app.currencyconverter.data.repository.CurrencyRepositoryImpl
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient) = Retrofit
        .Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor, loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .readTimeout(60,TimeUnit.SECONDS)
        .connectTimeout(60,TimeUnit.SECONDS)
        .addInterceptor(requestInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun providesLoggingInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit) = retrofit.create(CurrencyInstanceAPI::class.java)


    @Singleton
    @Provides
    fun provideCurrencyRepository(repo: CurrencyInstanceAPI) : CurrencyRepository {
        return CurrencyRepositoryImpl(repo)
    }
}