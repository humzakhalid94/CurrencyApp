package mhk.app.currencyconverter.domain.use_case

import kotlinx.coroutines.flow.Flow
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.common.base.Failure
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class SaveRecordUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    suspend fun invoke(record: RecordEntity) {
        return repository.saveRecord(record)
    }
}