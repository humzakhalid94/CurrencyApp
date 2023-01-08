package mhk.app.currencyconverter.presentation.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import mhk.app.currencyconverter.domain.use_case.GetCurrenciesUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val currencyUseCase: GetCurrenciesUseCase) : ViewModel() {

    private val state = MutableStateFlow<HomeMainFragmentState>(HomeMainFragmentState.Init)
    val mState: StateFlow<HomeMainFragmentState> get() = state

    private val currencies = MutableStateFlow<List<CurrencyEntity>>(mutableListOf())
    val mCurrencies: StateFlow<List<CurrencyEntity>> get() = currencies

    private var allCurrenySymbols = arrayListOf<String>()
    var selectedFrom: String? = null
    var selectedTo: String? = null

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

        } ?: kotlin.run{
            return allCurrenySymbols.toTypedArray()
        }

    }

    fun getToCurrencies(): Array<String> {
        selectedFrom?.let {
            return allCurrenySymbols.filter {
                it != selectedFrom
            }.toTypedArray()

        } ?: kotlin.run{
            return allCurrenySymbols.toTypedArray()
        }
    }

    fun validateCurrencyFields() : Boolean{
        if (selectedFrom == null || selectedTo == null) {
            return false
        }
        return true
    }

    fun swapCurrency() {
        val from = selectedFrom
        val to = selectedTo

        selectedFrom = to
        selectedTo = from
    }
    
}


sealed class HomeMainFragmentState {
    object Init : HomeMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeMainFragmentState()
    data class ShowToast(val message: String) : HomeMainFragmentState()
}