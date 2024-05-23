package uz.mahmudxon.currency.data.cache.mapper

import uz.mahmudxon.currency.data.base.DomainMapper
import uz.mahmudxon.currency.data.cache.tables.CurrencyTable
import uz.mahmudxon.currency.model.Currency

class CurrencyMapper : DomainMapper<CurrencyTable, Currency> {
    override fun mapToDomain(type: CurrencyTable): Currency {
        return Currency(
            code = type.code,
            name = type.name,
            rate = type.rate,
            date = type.date,
            diff = type.diff
        )
    }

    override fun mapFromDomain(type: Currency): CurrencyTable {
        return CurrencyTable().apply {
            code = type.code
            name = type.name
            rate = type.rate
            date = type.date
            diff = type.diff
        }
    }
}