package mhk.app.currencyconverter.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mhk.app.currencyconverter.R
import mhk.app.currencyconverter.databinding.FragmentDetailBinding
import mhk.app.currencyconverter.databinding.FragmentHomeBinding
import mhk.app.currencyconverter.presentation.extension.gone
import mhk.app.currencyconverter.presentation.extension.showToast
import mhk.app.currencyconverter.presentation.extension.visible

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchRecords()

        setUpObservers()
    }

    fun setUpObservers() {
        viewModel.records
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { currency ->
                Log.d("Records", "Content")
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }




    private fun handleState(state: DetailFragmentState){
        when(state){
            is DetailFragmentState.IsLoading -> handleLoading(state.isLoading)
            is DetailFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is DetailFragmentState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingProgressBar.visible()
        }else{
            binding.loadingProgressBar.gone()
        }
    }


}