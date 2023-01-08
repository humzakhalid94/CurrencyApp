package mhk.app.currencyconverter.domain.use_case

import kotlinx.coroutines.flow.Flow
import mhk.app.currencyconverter.data.remote.data_source.dto.BaseCurrencyDTO
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetBaseCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    suspend fun invoke(base: String) : Flow<BaseResult<BaseCurrencyEntity, BaseCurrencyDTO>> {
        return repository.getBaseCurrency(base)
    }

}