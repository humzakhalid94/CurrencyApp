package mhk.app.currencyconverter.domain.use_case

import kotlinx.coroutines.flow.Flow
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.common.base.Failure
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class FetchDataUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    suspend fun invoke(): Flow<BaseResult<List<RecordEntity>, Failure>> {
        return repository.findLast3Days()
    }
}