package uz.mahmudxon.currency.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

fun <T, V> CoroutineScope.asyncAll(
    items: Iterable<T>,
    function: suspend (T) -> V
): List<Deferred<V>>
{
    return items.map { async { function.invoke(it) } }
}