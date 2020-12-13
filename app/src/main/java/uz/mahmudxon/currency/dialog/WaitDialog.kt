package uz.mahmudxon.currency.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import uz.mahmudxon.currency.R

class WaitDialog(context: Context) : AlertDialog(context) {
    init {
        val view  = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null, false)
        setView(view)
        setCancelable(false)
    }
}