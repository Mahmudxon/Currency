package uz.mahmudxon.currency.data.cache.mapper

import uz.mahmudxon.currency.data.base.DomainMapper
import uz.mahmudxon.currency.data.cache.tables.ChartTable
import uz.mahmudxon.currency.model.Chart

class ChartMapper(private var currencyCode: String) : DomainMapper<ChartTable, Chart> {
    override fun mapToDomain(type: ChartTable): Chart {
        return Chart(
            date = type.date,
            rate = type.rate
        )
    }

    override fun mapFromDomain(type: Chart): ChartTable {
        return ChartTable()
            .apply {
                code = currencyCode
                date = type.date
                rate = type.rate
            }
    }
}