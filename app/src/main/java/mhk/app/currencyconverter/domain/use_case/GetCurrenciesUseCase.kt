package mhk.app.currencyconverter.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mhk.app.currencyconverter.common.Events
import mhk.app.currencyconverter.data.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyGenericDTO
import mhk.app.currencyconverter.data.data_source.dto.Rates
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    suspend fun invoke() : Flow<BaseResult<List<CurrencyEntity>, CurrencyDTO>> {
        return repository.getCurrencyList()
    }

}