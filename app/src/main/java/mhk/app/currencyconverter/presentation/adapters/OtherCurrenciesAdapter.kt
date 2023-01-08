package mhk.app.currencyconverter.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mhk.app.currencyconverter.databinding.ItemOtherCurrencyBinding
import mhk.app.currencyconverter.domain.model.OtherCurrencyEntity

class OtherCurrenciesAdapter (private val records: MutableList<OtherCurrencyEntity>) : RecyclerView.Adapter<OtherCurrenciesAdapter.ViewHolder>() {
    interface Listener {
        fun onTap(item: OtherCurrencyEntity)
    }

    private var listener: Listener? = null


    fun setOnTapListener(l : Listener){
        listener = l
    }


    fun updateList(list: List<OtherCurrencyEntity>){
        records.clear()
        records.addAll(list)
        notifyDataSetChanged()
    }



    inner class ViewHolder(private val itemBinding: ItemOtherCurrencyBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(item: OtherCurrencyEntity){
            itemBinding.tvSymbol.text = item.symbol
            itemBinding.tvRate.text = item.rate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemOtherCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(records[position])

    override fun getItemCount() = records.size
}