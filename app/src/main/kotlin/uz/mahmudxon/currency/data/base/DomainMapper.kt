package uz.mahmudxon.currency.data.base

interface DomainMapper<T, D> {
    fun mapToDomain(type: T): D
    fun mapFromDomain(type: D): T
}