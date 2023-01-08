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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mhk.app.currencyconverter.R
import mhk.app.currencyconverter.databinding.FragmentDetailBinding
import mhk.app.currencyconverter.databinding.FragmentHomeBinding
import mhk.app.currencyconverter.domain.model.BaseCurrencyEntity
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.model.OtherCurrencyEntity
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.presentation.adapters.OtherCurrenciesAdapter
import mhk.app.currencyconverter.presentation.adapters.RecordAdapter
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
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchRecords()
        setUpRecyclerView()
        setUpObservers()

        arguments?.getParcelable<BaseCurrencyEntity>("baseCurrency")?.let {
            handleOtherCurrency(it)
        }

    }

    private fun handleOtherCurrency(it: BaseCurrencyEntity) {
        binding.rvOtherCurrencies.adapter?.let { adapter ->

            val baseCurrency = mutableListOf<OtherCurrencyEntity>()
            var currency: OtherCurrencyEntity?
            it.rates.forEach { (key, value) ->
                currency = OtherCurrencyEntity(
                    symbol = key,
                    rate = value
                )
                baseCurrency.add(currency!!)
            }

            if (adapter is OtherCurrenciesAdapter) {
                adapter.updateList(baseCurrency)
            }
        }
    }

    fun setUpRecyclerView() {
        val recordAdapter = RecordAdapter(mutableListOf())
        recordAdapter.setOnTapListener(object: RecordAdapter.Listener{
            override fun onTap(item: RecordEntity) {
                println(item.date)
            }
        })

        binding.rvRecords.apply {
            adapter = recordAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        val otherCurrencyAdapter = OtherCurrenciesAdapter(mutableListOf())
        binding.rvOtherCurrencies.apply {
            adapter = otherCurrencyAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

    }

    fun setUpObservers() {
        viewModel.records
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { records ->
                Log.d("Records", "Content")
                handleRecords(records)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }


    private fun handleRecords(records: List<RecordEntity>){
        binding.rvRecords.adapter?.let { adapter ->
            if(adapter is RecordAdapter){
                adapter.updateList(records)
            }
        }
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