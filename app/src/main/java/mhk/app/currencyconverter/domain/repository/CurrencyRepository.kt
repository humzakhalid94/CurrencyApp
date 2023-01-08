package mhk.app.currencyconverter.domain.repository

import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import mhk.app.currencyconverter.data.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyGenericDTO
import mhk.app.currencyconverter.data.data_source.dto.Rates
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.model.CurrencyEntity

interface CurrencyRepository {
    suspend fun getCurrencyList() : Flow<BaseResult<List<CurrencyEntity>, CurrencyDTO>>

    suspend fun getBaseCurrency(base: String) : Flow<BaseResult<BaseCurrencyEntity, BaseCurrencyDTO>>
}