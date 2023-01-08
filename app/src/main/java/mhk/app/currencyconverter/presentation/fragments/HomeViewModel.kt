package mhk.app.currencyconverter.presentation.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import mhk.app.currencyconverter.domain.use_case.GetBaseCurrencyUseCase
import mhk.app.currencyconverter.domain.use_case.GetCurrenciesUseCase
import mhk.app.currencyconverter.domain.use_case.SaveRecordUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val currencyUseCase: GetCurrenciesUseCase,
    val baseCurrencyUseCase: GetBaseCurrencyUseCase,

    val saveRecordUseCase: SaveRecordUseCase
) : ViewModel() {

    private val state = MutableStateFlow<HomeMainFragmentState>(HomeMainFragmentState.Init)
    val mState: StateFlow<HomeMainFragmentState> get() = state

    private val currencies = MutableStateFlow<List<CurrencyEntity>>(mutableListOf())
    val mCurrencies: StateFlow<List<CurrencyEntity>> get() = currencies

    private var allCurrenySymbols = arrayListOf<String>()
    var selectedFrom = MutableLiveData<String?>()
    var selectedTo: String? = null

    var conversionAmount = MutableLiveData<Double>()


    init {
        getCurrencies()
    }


    private fun setLoading() {
        state.value = HomeMainFragmentState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = HomeMainFragmentState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = HomeMainFragmentState.ShowToast(message)
    }

    fun getCurrencies() {
        viewModelScope.launch {
            currencyUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            currencies.value = result.data
                            currencies.value.forEach {
                                allCurrenySymbols.add(it.symbol)
                            }

                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }


    }

    fun getFromCurrencies(): Array<String> {
        selectedTo?.let {
            return allCurrenySymbols.filter {
                it != selectedTo
            }.toTypedArray()

        } ?: kotlin.run {
            return allCurrenySymbols.toTypedArray()
        }

    }

    fun getToCurrencies(): Array<String> {
        selectedFrom.value?.let {
            return allCurrenySymbols.filter {
                it != selectedFrom.value
            }.toTypedArray()

        } ?: kotlin.run {
            return allCurrenySymbols.toTypedArray()
        }
    }

    fun validateCurrencyFields(): Boolean {
        if (selectedFrom.value == null || selectedTo == null) {
            return false
        }
        return true
    }

    fun setFromCurrency(value: String) {
        selectedFrom.value = value
    }

    fun swapCurrency() {
        val from = selectedFrom.value
        val to = selectedTo

        selectedFrom.value = to
        selectedTo = from
    }

    private val baseCurrency = MutableStateFlow<BaseCurrencyEntity>(BaseCurrencyEntity())
    val mBaseCurrency: StateFlow<BaseCurrencyEntity> get() = baseCurrency

    fun getBaseCurrency(fromCurrency: String) = viewModelScope.launch {
        baseCurrencyUseCase.invoke(fromCurrency)
            .onStart {
                setLoading()
            }
            .catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
            }
            .collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.Success -> {
                        baseCurrency.value = result.data

                    }
                    is BaseResult.Error -> {
                        showToast(result.rawResponse.message)
                    }
                }
            }
    }

    fun getToCurrencyValue(): Double {
        val map = baseCurrency.value.rates

        map.get(selectedTo)?.let {
            return it.toDouble()
        }

        return 0.0
    }

    fun performConversion(amount: Int) {
        val difference = getToCurrencyValue()
        conversionAmount.value = amount * difference
    }

    fun saveRecord() = viewModelScope.launch {
        if (!validateCurrencyFields()) { return@launch }

        val from = selectedFrom.value!!
        val to = selectedTo!!

        saveRecordUseCase.invoke(RecordEntity(from = from, to = to))
    }

}


sealed class HomeMainFragmentState {
    object Init : HomeMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeMainFragmentState()
    data class ShowToast(val message: String) : HomeMainFragmentState()
}