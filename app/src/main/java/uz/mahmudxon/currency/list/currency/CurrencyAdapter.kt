package uz.mahmudxon.currency.list.currency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.model.Currency

class CurrencyAdapter : RecyclerView.Adapter<CurrencyViewHolder>() {
    private val data = ArrayList<Currency>()

    var listener: ICurrencyListItemClickListener? = null

    fun setData(data: List<Currency>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.setOnClickListener { listener?.onItemClick(data[position]) }
    }

    override fun getItemCount() = data.size
}