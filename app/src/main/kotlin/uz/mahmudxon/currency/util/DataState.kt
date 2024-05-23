package uz.mahmudxon.currency.util

data class DataState<T>(
    val error: Exception? = null,
    val data: T? = null,
    val isLoading: Boolean = false
) {

    companion object {

        fun <T> error(
            error: Exception,
        ): DataState<T> {
            return DataState(
                error = (if (error.cause
                    != null && error.cause is Exception
                ) error.cause else error) as Exception?,
                data = null,
            )
        }

        fun <T> data(
            data: T? = null,
        ): DataState<T> {
            return DataState(
                data = data,
                error = null
            )
        }

        fun <T> loading(): DataState<T> = DataState(isLoading = true)
    }
}
