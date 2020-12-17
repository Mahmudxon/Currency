package uz.mahmudxon.currency.ui.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.ext.scope
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.databinding.FragmentMainBinding
import uz.mahmudxon.currency.dialog.AboutDialog
import uz.mahmudxon.currency.list.currency.CurrencyAdapter
import uz.mahmudxon.currency.list.currency.ICurrencyListItemClickListener
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.ui.base.BaseFragment
import uz.mahmudxon.currency.util.*

class MainFragment : BaseFragment(R.layout.fragment_main), DatePickerDialog.OnDateSetListener,
    ICurrencyListItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private val vm: MainViewModel by scope.viewModel(this)
    private val adapter: CurrencyAdapter by scope.inject()
    private lateinit var binding: FragmentMainBinding
    private val navController: NavController by lazy { findNavController() }

    override fun onCreate(view: View) {
        binding = FragmentMainBinding.bind(view)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.list.adapter = adapter
        adapter.listener = this
        binding.menu.setOnClickListener { binding.drawer.openDrawer(GravityCompat.START) }
        binding.calendar.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), this, 2020, 12, 11)
            val today = DateTime()
            val m180DayAgo = today.minusDays(180)
            dialog.datePicker.minDate = m180DayAgo.millis
            dialog.datePicker.maxDate = today.millis
            dialog.show()
        }
        vm.getData().observe(this, { onSuccess(it) })
        vm.getError()
            .observe(this, { it.getContentIfNotHandled()?.let { error -> onError(error) } })
        vm.requestDataFromCBU()
        binding.navView.setOnKeyListener(this)
        vm.loading.observe(this, {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun showLoading() {
        binding.list.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.list.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
    }

    private fun onSuccess(data: List<Currency>) {
        Log.d(TAG, "onSuccess: $data")
        adapter.setData(data)
    }

    private fun onError(e: Exception) {
        Log.d(TAG, "onError: ${e.message}")
        navController.navigate(R.id.action_mainFragment_to_errorFragment)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onDateSet: $year - $month - $dayOfMonth")
        binding.title.text = "%02d-%02d-%04d".format(dayOfMonth, month + 1, year)
        vm.requestDataFromCBU(date = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth))
    }

    override fun onItemClick(item: Currency) {
        Log.d(TAG, "onItemClick: $item")
        navController.navigate(
            R.id.action_mainFragment_to_converterFragment,
            bundleOf("ccy" to item.Ccy)
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.facebook -> openUrlIntent(facebook)
            R.id.telegram -> openUrlIntent(telegram)
            R.id.github -> openUrlIntent(github)
            R.id.instagram -> openUrlIntent(instagram)
            R.id.play_store -> openUrlIntent(playStore)
            R.id.twitter -> openUrlIntent(twitter)
            R.id.feedback -> navController.navigate(R.id.action_mainFragment_to_feedbackFragment)
            R.id.info -> AboutDialog(requireContext()).show()
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openUrlIntent(url: String) =
        activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}