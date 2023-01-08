package mhk.app.currencyconverter.domain.repository

import kotlinx.coroutines.flow.Flow
import mhk.app.currencyconverter.data.remote.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.data.remote.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.common.base.Failure
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.model.RecordEntity

interface CurrencyRepository {
    suspend fun getCurrencyList(): Flow<BaseResult<List<CurrencyEntity>, CurrencyDTO>>

    suspend fun getBaseCurrency(base: String): Flow<BaseResult<BaseCurrencyEntity, BaseCurrencyDTO>>

    suspend fun findLast3Days(): Flow<BaseResult<List<RecordEntity>, Failure>>

    suspend fun saveRecord(record: RecordEntity)
}