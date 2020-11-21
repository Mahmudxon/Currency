package uz.mahmudxon.currency.ui.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout),
    View.OnKeyListener {
    private var isUseBackPress = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(this)
        onCreate(view)
    }

    abstract fun onCreate(view: View)

    open fun onBackPressed() {
        isUseBackPress = false
    }

    fun finish() {
        findNavController().popBackStack()
    }

    fun hideKeyBoard() {
        val view = activity?.currentFocus ?: View(activity)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(editText, 0)
    }

    override fun onKey(p0: View?, keyCode: Int, e: KeyEvent?): Boolean {
        // back press
        if (keyCode == KeyEvent.KEYCODE_BACK && e?.action == KeyEvent.ACTION_DOWN) {
            isUseBackPress = true
            onBackPressed()
            return isUseBackPress
        }
        return false
    }

    fun toast(@StringRes resID: Int) {
        Toast.makeText(context, resID, Toast.LENGTH_SHORT).show()
    }

    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}