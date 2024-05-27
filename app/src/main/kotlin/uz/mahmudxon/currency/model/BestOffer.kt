package uz.mahmudxon.currency.model

data class BestOffer(
    val price: Double = 0.0,
    val banks: List<Bank> = emptyList()
)