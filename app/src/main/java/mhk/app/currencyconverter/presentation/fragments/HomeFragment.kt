package mhk.app.currencyconverter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import mhk.app.currencyconverter.R
import mhk.app.currencyconverter.databinding.FragmentHomeBinding
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mhk.app.currencyconverter.presentation.extension.gone
import mhk.app.currencyconverter.presentation.extension.showToast
import mhk.app.currencyconverter.presentation.extension.visible

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetail.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }

        binding.btnSwap.setOnClickListener {

            if (!viewModel.validateCurrencyFields()) {
                requireActivity().showToast("Select both currencies to swap")

                return@setOnClickListener
            }

            val from = viewModel.selectedFrom
            val to = viewModel.selectedTo

            binding.tvFromCountry.text = to
            binding.tvToCountry.text = from

            viewModel.swapCurrency()

        }

        binding.tvFromCountry.setOnClickListener {
            val currencies = viewModel.getFromCurrencies()
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(resources.getString(R.string.select_from))
                .setItems(currencies) { dialog, which ->
                    viewModel.selectedFrom = currencies.get(which)
                    binding.tvFromCountry.text = viewModel.selectedFrom
                }
                .show()
        }

        binding.tvToCountry.setOnClickListener {
            val currencies = viewModel.getToCurrencies()
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(resources.getString(R.string.select_to))
                .setItems(currencies) { dialog, which ->
                    viewModel.selectedTo = currencies.get(which)
                    binding.tvToCountry.text = viewModel.selectedTo
                }
                .show()
        }

        setUpObservers()
    }

    fun setUpObservers() {

        viewModel.mCurrencies
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { currency ->
//                handleCurrency(currency)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


    }



    private fun handleState(state: HomeMainFragmentState){
        when(state){
            is HomeMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is HomeMainFragmentState.Init -> Unit
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