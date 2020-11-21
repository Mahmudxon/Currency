package uz.mahmudxon.currency.list.currency

import uz.mahmudxon.currency.model.Currency

interface ICurrencyListItemClickListener {
    fun onItemClick(item: Currency)
}