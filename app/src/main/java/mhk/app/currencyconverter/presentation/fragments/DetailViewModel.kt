package mhk.app.currencyconverter.presentation.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.domain.use_case.FetchDataUseCase
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor (private val fetchDataUseCase: FetchDataUseCase) : ViewModel() {

    private val _state = MutableStateFlow<DetailFragmentState>(DetailFragmentState.Init)
    val state : StateFlow<DetailFragmentState> get() = _state


    private val _records = MutableStateFlow(mutableListOf<RecordEntity>())
    val records : StateFlow<List<RecordEntity>> get() = _records

    private fun setLoading(){
        _state.value = DetailFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = DetailFragmentState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = DetailFragmentState.ShowToast(message)
    }

    fun fetchRecords(){
        viewModelScope.launch {

            fetchDataUseCase.invoke()
                .onStart {
                    setLoading()
                }.flowOn(Dispatchers.IO)
                .catch { e ->
                    hideLoading()
                    showToast(e.message.toString())
                }
                .collect { result ->
                    hideLoading()

                    when(result) {
                        is BaseResult.Success -> {
                            _records.value = result.data as MutableList<RecordEntity>
                        }
                        is BaseResult.Error -> {
                            // 0 means no internet connection
                            if(result.rawResponse.code != 0){
                                val msg = "${result.rawResponse.message} [${result.rawResponse.code}]"
                                showToast(msg)
                            }

                        }

                    }


                }

        }

    }


}





sealed class DetailFragmentState {
    object Init : DetailFragmentState()
    data class ShowToast(val message : String) : DetailFragmentState()
    data class IsLoading(val isLoading: Boolean) : DetailFragmentState()
}