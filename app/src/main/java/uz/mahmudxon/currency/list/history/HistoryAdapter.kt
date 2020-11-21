package uz.mahmudxon.currency.list.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.util.insertIfNotExists

class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder>() {
    private val data = ArrayList<Currency>()

    fun setData(data: List<Currency>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: Currency) {
        //data.add(item)
        data.insertIfNotExists(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_currency, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}