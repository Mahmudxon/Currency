package uz.mahmudxon.currency.ui.currencyDetails

import uz.mahmudxon.currency.model.BankPrice
import uz.mahmudxon.currency.model.BestOffer
import uz.mahmudxon.currency.model.Chart
import uz.mahmudxon.currency.model.Currency

data class CurrencyDetailsState(
    val isLoading: Boolean = false,
    val error: Int = -1,
    val currency: Currency? = null,
    val chart: List<Chart> = emptyList(),
    val sellingAvailable: Boolean = false,
    val sellingLoading: Boolean = false,
    val bankPrices: List<BankPrice> = emptyList(),
    val isForeignCurrencyTop: Boolean = true,
    val localValue: String = "",
    val foreignValue: String = "",
    val bestOfferForSelling: BestOffer = BestOffer(),
    val bestOfferForBuying: BestOffer = BestOffer(),
    val showBankPrice: BankPrice? = null
)