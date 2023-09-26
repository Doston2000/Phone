package uz.lazydevelopers1.phone.ui.logInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.ActivityLogInfoBinding
import uz.lazydevelopers1.phone.moduls.LogsGroupModule
import uz.lazydevelopers1.phone.ui.logInfo.adapters.LogInfoGroupAdapter
import uz.lazydevelopers1.phone.ui.splashPermissions.PermissionsActivity
import uz.lazydevelopers1.phone.utils.CommunicationOptions
import uz.lazydevelopers1.phone.utils.RequestPermission

class LogInfoActivity : AppCompatActivity() {

    private lateinit var logInfoBinding: ActivityLogInfoBinding
    private lateinit var logHistoryList: ArrayList<LogsGroupModule>
    private lateinit var logInfoGroupAdapter: LogInfoGroupAdapter
    private lateinit var logInfoViewModel: LogInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInfoBinding = ActivityLogInfoBinding.inflate(layoutInflater)
        setContentView(logInfoBinding.root)

        logInfoViewModel = LogInfoViewModel(contentResolver)
        logHistoryList = ArrayList()

        logInfoBinding.apply {
            logInfoGroupAdapter = LogInfoGroupAdapter(logHistoryList, this@LogInfoActivity)
            rv.adapter = logInfoGroupAdapter

            val name = LogInfoViewModel.LogInfo.log?.name
            val number = LogInfoViewModel.LogInfo.log?.number
            val image = LogInfoViewModel.LogInfo.log?.img

            logName.text = name ?: number ?: ""
            logNumber.text = number ?: ""
            if (image != null) {
                logImg.setImageBitmap(image)
            } else {
                logImg.setImageResource(R.drawable.ic_user)
                logImg.setPadding(50)
            }

            back.setOnClickListener {
                finish()
            }

            call.setOnClickListener {
                call(number ?: "")
            }

            message.setOnClickListener {
                message(number ?: "")
            }

            showAll.setOnClickListener {
                it.visibility = View.GONE
                loadData(number ?: "", true)
            }

            loadData(number ?: "", false)
        }

        getLogsHistory()
    }

    private fun loadData(number: String, all: Boolean) {
        logInfoBinding.progress.visibility = View.VISIBLE
        logInfoViewModel.getHistory(number, all)
    }

    private fun call(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            if (RequestPermission(this).checkCallPhonePermission()) {
                CommunicationOptions.call(phoneNumber, this)
            } else {
                val intent = Intent(this, PermissionsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun message(number: String) {
        if (number.isNotEmpty()) {
            if (RequestPermission(this).checkSendSmsPermission()
            ) {
                CommunicationOptions.message(this, number)
            } else {
                val intent = Intent(this, PermissionsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getLogsHistory() {
        LogInfoViewModel.LogInfo.logHistoryList.observe(this) {
            if (it != null) {
                logHistoryList.clear()
                logHistoryList.addAll(it)
                logInfoGroupAdapter.notifyDataSetChanged()
            }
            logInfoBinding.progress.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logInfoViewModel.clearLogInfo()
    }
}