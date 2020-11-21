package uz.mahmudxon.currency.util

const val TAG = "TTT"

fun <T : Comparable<T>> ArrayList<T>.insertUniqueItem(item: T) {
    if (this.contains(item))
        return
    this.add(item)
}

fun <E> ArrayList<E>.insertIfNotExists(item: E) {
    if (this.contains(item))
        return
    this.add(item)
}