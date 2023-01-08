package mhk.app.currencyconverter.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
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
import mhk.app.currencyconverter.presentation.extension.roundOffDecimal
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

            val from = viewModel.selectedFrom?.value
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
                    viewModel.selectedFrom?.value = currencies.get(which)
                    binding.tvFromCountry.text = viewModel.selectedFrom?.value
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

        viewModel.conversionAmount.observe(viewLifecycleOwner, Observer {
            binding.etTo.setText("${it.roundOffDecimal()}")
        })

        viewModel.selectedFrom.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.getBaseCurrency(it)
            }
        })

        viewModel.mCurrencies
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { currency ->
//                handleCurrency(currency)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.mBaseCurrency
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { currency ->
                performCalculation(binding.etFrom.text)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


        binding.etFrom.addTextChangedListener(amountWatcher)

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

    var amountWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            performCalculation(charSequence)

        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun performCalculation(charSequence: CharSequence) {
        val input = charSequence.toString()

        if (input.isEmpty()) {
            binding.etTo.setText("")
            return
        }

        val amount = input.toIntOrNull()

        amount?.let {
            viewModel.performConversion(it)
        }

    }


}