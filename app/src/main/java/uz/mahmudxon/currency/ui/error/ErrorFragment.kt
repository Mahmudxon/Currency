package uz.mahmudxon.currency.ui.error

import android.view.View
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.databinding.FragmentErrorBinding
import uz.mahmudxon.currency.ui.base.BaseFragment

class ErrorFragment : BaseFragment(R.layout.fragment_error) {
    lateinit var binding: FragmentErrorBinding
    override fun onCreate(view: View) {
        binding = FragmentErrorBinding.bind(view)
        binding.refresh.setOnClickListener { finish() }
        binding.quit.setOnClickListener { activity?.finish() }
    }

    override fun onBackPressed() {
        // nothing :)
    }
}