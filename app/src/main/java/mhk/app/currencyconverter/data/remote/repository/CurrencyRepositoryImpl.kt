package mhk.app.currencyconverter.data.remote.repository

import mhk.app.currencyconverter.data.remote.CurrencyInstanceAPI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext
import mhk.app.currencyconverter.data.local.CurrencyDao
import mhk.app.currencyconverter.data.remote.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.data.remote.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.common.base.Failure
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyInstanceAPI,
    private val currencyDao: CurrencyDao

) : CurrencyRepository {


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
                val base = body.base

                val baseCurrency = BaseCurrencyEntity(base = base, rates = data)
                emit(BaseResult.Success(baseCurrency))
            } else {
                val type = object : TypeToken<BaseCurrencyDTO>(){}.type
                val err = Gson().fromJson<BaseCurrencyDTO>(response.errorBody()!!.charStream(), type)!!
                emit(BaseResult.Error(err))
            }

        }
    }


    override suspend  fun saveRecord(record: RecordEntity) {
        withContext(Dispatchers.IO) {
            currencyDao.insert(record)
        }
    }


    override suspend fun findLast3Days(): Flow<BaseResult<List<RecordEntity>, Failure>> {
        return flow {
                val localTodos = currencyDao.findLast3Days()
                emit(BaseResult.Success(localTodos))
        }
    }


}

