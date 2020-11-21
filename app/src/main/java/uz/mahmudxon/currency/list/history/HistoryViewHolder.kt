package uz.mahmudxon.currency.list.history

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.databinding.ItemCurrencyBinding
import uz.mahmudxon.currency.model.Currency

class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding by lazy { ItemCurrencyBinding.bind(itemView) }

    @SuppressLint("SetTextI18n")
    fun bind(data: Currency) {
        binding.currency.text = data.Date
        binding.value.text = data.Rate + " so\'m"
        when {
            data.Diff.startsWith("-") -> {
                binding.desc.text = data.Diff
                binding.icon.setImageResource(R.drawable.ic_baseline_trending_down_24)
                val red = ContextCompat.getColor(itemView.context, R.color.red)
                binding.desc.setTextColor(red)
            }
            data.Diff == "0" -> {
                binding.desc.text = ""
                binding.icon.setImageDrawable(null)
                val color = ContextCompat.getColor(itemView.context, R.color.textColor)
                binding.desc.setTextColor(color)
            }
            else -> {
                binding.desc.text = "+${data.Diff}"
                binding.icon.setImageResource(R.drawable.ic_baseline_trending_up_24)
                val green = ContextCompat.getColor(itemView.context, R.color.green)
                binding.desc.setTextColor(green)
            }
        }
    }
}