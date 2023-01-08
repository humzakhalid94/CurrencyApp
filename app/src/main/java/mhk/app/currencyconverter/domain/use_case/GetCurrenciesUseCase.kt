package mhk.app.currencyconverter.domain.use_case

import kotlinx.coroutines.flow.Flow
import mhk.app.currencyconverter.data.remote.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    suspend fun invoke() : Flow<BaseResult<List<CurrencyEntity>, CurrencyDTO>> {
        return repository.getCurrencyList()
    }

}