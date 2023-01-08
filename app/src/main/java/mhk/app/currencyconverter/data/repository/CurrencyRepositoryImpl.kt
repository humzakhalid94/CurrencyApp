package mhk.app.currencyconverter.data.repository

import com.example.anime.data.data_source.dto.CurrencyInstanceAPI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mhk.app.currencyconverter.data.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyGenericDTO
import mhk.app.currencyconverter.data.data_source.dto.Rates
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val currencyApi: CurrencyInstanceAPI) :
    CurrencyRepository {


    override suspend fun getCurrencyList(): Flow<BaseResult<List<CurrencyEntity>, CurrencyDTO>> {
        return flow {
            val response = currencyApi.getCurrencies()

            if (response.isSuccessful) {
                val body = response.body()!!
                val data = body.symbols

                val currency = mutableListOf<CurrencyEntity>()

                var currencyEntity: CurrencyEntity?
                data.forEach { (key, value) ->
//                    "AED": "United Arab Emirates Dirham",
                    currencyEntity = CurrencyEntity(
                        symbol = key,
                        name = value
                    )
                    currency.add(currencyEntity!!)
                }
                emit(BaseResult.Success(currency))
            } else {
                val type = object : TypeToken<CurrencyDTO>() {}.type
                val err = Gson().fromJson<CurrencyDTO>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getBaseCurrency(base: String): Flow<BaseResult<BaseCurrencyEntity, BaseCurrencyDTO>> {
        return flow {
            val response = currencyApi.getBaseCurrency(base)
            if (response.isSuccessful) {
                val body = response.body()!!
                val data = body.rates
//
                val baseCurrency = BaseCurrencyEntity(rates = data)
                emit(BaseResult.Success(baseCurrency))
            } else {
                val type = object : TypeToken<BaseCurrencyDTO>(){}.type
                val err = Gson().fromJson<BaseCurrencyDTO>(response.errorBody()!!.charStream(), type)!!
                emit(BaseResult.Error(err))
            }

        }
    }


}