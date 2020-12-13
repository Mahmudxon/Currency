package uz.mahmudxon.currency.ui.feedback

import android.view.View
import androidx.appcompat.app.AlertDialog
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import org.koin.ext.scope
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.databinding.FragmentFeedbackBinding
import uz.mahmudxon.currency.ui.base.BaseFragment

class FeedbackFragment : BaseFragment(R.layout.fragment_feedback), View.OnClickListener,
    BackgroundMail.OnSendingCallback {
    lateinit var binding: FragmentFeedbackBinding
    private val sender: BackgroundMail.Builder by scope.inject()
    private var waitDialog: AlertDialog? = null
    override fun onCreate(view: View) {
        binding = FragmentFeedbackBinding.bind(view)
        binding.send.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.send -> sendFeedBack()
            R.id.cancel -> finish()
        }
    }

    private fun sendFeedBack() {
        hideKeyBoard()
        val sender = binding.sender.text.toString()
        val contact = binding.contact.text.toString()
        val message = binding.message.text.toString()
        val appname = context?.getString(R.string.app_name)
        if (message.length > 3 && sender.length > 3 && contact.length > 3) {
            waitDialog = AlertDialog.Builder(requireContext()).create()
            waitDialog?.setCancelable(false)
            waitDialog?.setContentView(R.layout.dialog_wait)
            waitDialog?.show()
            this.sender.withSubject("feedback from (android app) - $appname")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withUseDefaultSession(true)
                .withSenderName(sender)
                .withBody("$message\nContact: $contact")
                .withOnSuccessCallback(this)
                .send()
        } else {
            toast(R.string.need_3_characters_message)
        }
    }

    override fun onSuccess() {
        waitDialog?.dismiss()
        toast(R.string.send_success_feedback)
        finish()
    }

    override fun onFail(p0: Exception?) {
        waitDialog?.dismiss()
        toast(R.string.send_fail_feedback)
    }
}