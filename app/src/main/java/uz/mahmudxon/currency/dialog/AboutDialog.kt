package uz.mahmudxon.currency.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.databinding.DialogAboutBinding

class AboutDialog(context: Context) : AlertDialog(context) {
    lateinit var binding: DialogAboutBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_about, null, false)
        setView(view)
        binding = DialogAboutBinding.bind(view)
        binding.btnOk.setOnClickListener {
            dismiss()
        }
        setCancelable(true)
    }
}