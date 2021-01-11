package uz.mahmudxon.currency.ui.converter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.ext.scope
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.databinding.FragmentConverterBinding
import uz.mahmudxon.currency.list.history.HistoryAdapter
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.ui.base.BaseFragment
import uz.mahmudxon.currency.util.TAG
import uz.mahmudxon.currency.util.toNumberFormatString

class ConverterFragment : BaseFragment(R.layout.fragment_converter), TextWatcher {
    lateinit var binding: FragmentConverterBinding
    private val adapter: HistoryAdapter by scope.inject()
    private val vm: ConverterViewModel by scope.viewModel(this)
    private val navController: NavController by lazy { findNavController() }
    private var currency: Double = 0.0
    private var isReversed = false

    override fun onCreate(view: View) {
        binding = FragmentConverterBinding.bind(view)
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.edt1.addTextChangedListener(this)
        vm.getToday().observe(this, { getToday(it) })
        vm.getData().observe(this, { getHistoryData(it) })
        vm.getError()
            .observe(this, { it.getContentIfNotHandled()?.let { error -> getError(error) } })
        arguments?.let {
            vm.getData(it.getString("ccy", "usd"))
        }
        binding.reverse.setOnClickListener { reverse() }
        binding.cancel.setOnClickListener { hideKeyBoard(); finish() }
        vm.loading.observe(this, {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun getToday(currency: Currency) {
        adapter.clearData()
        adapter.addItem(currency)
        this.currency = currency.Rate.toDouble()
        binding.primaryCurrency.text = currency.CcyNm_UZ
        binding.secondaryCurrency.text = context?.getText(R.string.uzbek_summ)
        binding.edt1.setText("1")
    }

    private fun getHistoryData(currency: Currency) {
        adapter.addItem(currency)
        Log.d(TAG, "getHistoryData: $currency")
    }

    private fun getError(exception: Exception) {
        Log.d(TAG, "getError: $exception")
        navController.navigate(R.id.action_converterFragment_to_errorFragment)
    }

    private fun reverse() {
        val s = binding.primaryCurrency.text
        binding.primaryCurrency.text = binding.secondaryCurrency.text
        binding.secondaryCurrency.text = s
        isReversed = !isReversed
        calculate()
    }

    private fun calculate() {
        binding.edt2.setText("0")
        try {
            val d = binding.edt1.text.toString().toDouble()
            val result = if (isReversed) d / currency else d * currency
            /*val formatter: NumberFormat = NumberFormat.getCurrencyInstance()
            val moneyString: String = formatter.format(result).replace("$", "")*/
            binding.edt2.setText(result.toNumberFormatString())
        } catch (e: Exception) {
            Log.d(TAG, "calculate: $e")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s != null) {
            calculate()
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun showLoading() {
        binding.content.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.content.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
    }

}