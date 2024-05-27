package uz.mahmudxon.currency.model

data class Bank(
    val id: Int,
    val name: String,
    val logo: String,
    val website: String
) {
    enum class Id {
        NBU, KAPITAL, AgroBank, ALOQA, TURON, DavrBank, Ofb, MkBank, SQB, IpotekaBank, XB, HamkorBank, InfinBank
    }
}