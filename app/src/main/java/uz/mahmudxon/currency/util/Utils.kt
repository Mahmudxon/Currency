package uz.mahmudxon.currency.util

const val TAG = "TTT"

fun <E> ArrayList<E>.insertIfNotExists(item: E) {
    if (this.contains(item))
        return
    this.add(item)
}

fun Int.toNumberFormatString(): String {
    var result = ""
    var n = 0
    this.toString().reversed().forEach { char ->
        if (n % 3 == 0 && n != 0)
            result = ",$result"
        result = char + result
        n++
    }
    return result
}

fun String.safeCut(size: Int): String {
    return if (this.length > size)
        this.subSequence(0, size).toString()
    else this
}

fun Double.toNumberFormatString(): String {
    val whole = this.toInt()
    return whole.toNumberFormatString() + "." + "%.2f".format(this).split(".").last().safeCut(2)
}
