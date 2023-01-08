package mhk.app.currencyconverter.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mhk.app.currencyconverter.databinding.ItemRecordsBinding
import mhk.app.currencyconverter.domain.model.RecordEntity
import mhk.app.currencyconverter.presentation.extension.formatted

class RecordAdapter (private val records: MutableList<RecordEntity>) : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {
    interface Listener {
        fun onTap(item: RecordEntity)
    }

    private var listener: Listener? = null


    fun setOnTapListener(l : Listener){
        listener = l
    }


    fun updateList(list: List<RecordEntity>){
        records.clear()
        records.addAll(list)
        notifyDataSetChanged()
    }



    inner class ViewHolder(private val itemBinding: ItemRecordsBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(item: RecordEntity){
            itemBinding.tvCurrency.text = "${item.from} - ${item.to}"
            itemBinding.tvDate.text = item.date.formatted()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemRecordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(records[position])

    override fun getItemCount() = records.size
}