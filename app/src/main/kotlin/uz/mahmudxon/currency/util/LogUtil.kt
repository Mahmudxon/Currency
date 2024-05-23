package uz.mahmudxon.currency.util

import android.util.Log

fun Any.dlog(msg: Any) {
    Log.d("TTT", "${this::class.simpleName} : $msg")
}

