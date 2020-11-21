package uz.mahmudxon.currency.repo

sealed class VMResponse<T> {
    data class Success<T>(val data: T) : VMResponse<T>()
    data class Error<T>(val exception: Exception) : VMResponse<T>()
}