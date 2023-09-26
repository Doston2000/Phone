package uz.lazydevelopers1.phone.ui.logInfo.adapters

import android.annotation.SuppressLint
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.LogInfoItemBinding
import uz.lazydevelopers1.phone.moduls.LogModule

class LogInfoAdapter(private var logs: ArrayList<LogModule>) :
    RecyclerView.Adapter<LogInfoAdapter.LogInfoVH>() {

    inner class LogInfoVH(private var logInfoBinding: LogInfoItemBinding) :
        RecyclerView.ViewHolder(logInfoBinding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(logModule: LogModule, position: Int) {
            if (position == 0) {
                logInfoBinding.line.visibility = View.INVISIBLE
            } else {
                logInfoBinding.line.visibility = View.VISIBLE
            }

            logInfoBinding.userInfo.text = logModule.hhMMDate

            val hours = (logModule.duration?.toInt() ?: 0) / 60 / 60
            val minutes = ((logModule.duration?.toInt() ?: 0) / 60)
            val seconds = (logModule.duration?.toInt() ?: 0) % 60

            val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

            logInfoBinding.timeSpent.text = when (logModule.type) {
                CallLog.Calls.BLOCKED_TYPE -> {
                    logInfoBinding.logStatusIc.setImageResource(R.drawable.ic_phone_locked)
                    "Blocked Call"
                }

                CallLog.Calls.INCOMING_TYPE -> {
                    logInfoBinding.logStatusIc.setImageResource(R.drawable.ic_phone_callback)
                    "Incoming call"
                }

                CallLog.Calls.MISSED_TYPE -> {
                    logInfoBinding.logStatusIc.setImageResource(R.drawable.ic_phone_missed)
                    "Missed call"
                }

                CallLog.Calls.OUTGOING_TYPE -> {
                    logInfoBinding.logStatusIc.setImageResource(R.drawable.ic_phone_forwarded)
                    "Outgoing call"
                }

                CallLog.Calls.REJECTED_TYPE -> {
                    logInfoBinding.logStatusIc.setImageResource(R.drawable.ic_phone_disabled)
                    "Rejected call"
                }

                else -> {
                    logInfoBinding.logStatusIc.setImageResource(R.drawable.ic_phone)
                    "call"
                }

            } + ", " + timeString

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogInfoVH {
        return LogInfoVH(
            LogInfoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogInfoVH, position: Int) {
        holder.onBind(logs[position], position)
    }

    override fun getItemCount(): Int = logs.size

}