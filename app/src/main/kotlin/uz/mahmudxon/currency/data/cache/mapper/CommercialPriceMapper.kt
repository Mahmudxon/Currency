package uz.mahmudxon.currency.data.cache.mapper

import uz.mahmudxon.currency.data.base.DomainMapper
import uz.mahmudxon.currency.data.cache.tables.CommercialPriceTable
import uz.mahmudxon.currency.model.Bank
import uz.mahmudxon.currency.model.BankPrice

class CommercialPriceMapper : DomainMapper<CommercialPriceTable, BankPrice> {
    override fun mapToDomain(type: CommercialPriceTable): BankPrice {
        val bank = Bank(
            id = type.bankId,
            name = type.bankName,
            logo = type.bankLogo,
            website = type.bankWebsite,
            address = type.bankAddress,
            phone = type.bankPhone,
            licens = type.bankLicens,
            inn = type.bankInn
        )
        return BankPrice(
            bank = bank,
            currencyCode = type.currencyCode,
            date = type.date,
            buy = type.buy,
            sell = type.sell
        )
    }

    override fun mapFromDomain(type: BankPrice): CommercialPriceTable {
        return CommercialPriceTable(
            bankId = type.bank.id,
            bankName = type.bank.name,
            bankLogo = type.bank.logo,
            bankWebsite = type.bank.website,
            currencyCode = type.currencyCode,
            date = type.date,
            buy = type.buy,
            sell = type.sell,
            bankAddress = type.bank.address,
            bankPhone = type.bank.phone,
            bankLicens = type.bank.licens,
            bankInn = type.bank.inn
        )
    }
}