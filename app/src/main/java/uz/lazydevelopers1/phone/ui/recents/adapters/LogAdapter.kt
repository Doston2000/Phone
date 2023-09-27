package uz.lazydevelopers1.phone.ui.recents.adapters

import android.content.Context
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.LogItemBinding
import uz.lazydevelopers1.phone.moduls.LogModule

class LogAdapter(
    private var userLogsList: ArrayList<LogModule>,
    var recentClick: RecentClick,
    var context: Context
) :
    RecyclerView.Adapter<LogAdapter.LogVH>() {

    inner class LogVH(private var recentItemBinding: LogItemBinding) :
        RecyclerView.ViewHolder(recentItemBinding.root) {
        fun onBind(logModule: LogModule, position: Int) {

            if (SelectLogs.selectLogs.contains(logModule)) {
                recentItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.select))
            } else {
                recentItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.transparent))
            }

            recentItemBinding.type.text = when (logModule.type) {
                CallLog.Calls.BLOCKED_TYPE -> {
                    recentItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_locked)
                    "Blocked Call"
                }

                CallLog.Calls.INCOMING_TYPE -> {
                    recentItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_callback)
                    "Incoming call"
                }

                CallLog.Calls.MISSED_TYPE -> {
                    recentItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_missed)
                    "Missed call"
                }

                CallLog.Calls.OUTGOING_TYPE -> {
                    recentItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_forwarded)
                    "Outgoing call"
                }

                CallLog.Calls.REJECTED_TYPE -> {
                    recentItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_disabled)
                    "Rejected call"
                }

                else -> {
                    recentItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone)
                    "call"
                }

            }

            if (position == 0) {
                recentItemBinding.line.visibility = View.INVISIBLE
            } else {
                recentItemBinding.line.visibility = View.VISIBLE
            }

            val info = if (logModule.name?.isNotEmpty() == true) logModule.name else logModule.number

            recentItemBinding.userInfo.text = (info) + if (logModule.similarLogsIds.size > 1) " (${logModule.similarLogsIds.size})" else ""

            recentItemBinding.logTime.text = logModule.hhMMDate

            recentItemBinding.root.setOnClickListener {
                if (SelectLogs.selectLogs.size < 1) {
                    if (recentItemBinding.logBottomContainer.visibility == View.GONE) {
                        recentItemBinding.logBottomContainer.visibility = View.VISIBLE
                    } else if (recentItemBinding.logBottomContainer.visibility == View.VISIBLE) {
                        recentItemBinding.logBottomContainer.visibility = View.GONE
                    }
                } else {
                    selectLog(logModule)
                }
            }

            recentItemBinding.call.setOnClickListener {
                recentClick.call(logModule)
            }

            recentItemBinding.message.setOnClickListener {
                recentClick.message(logModule)
            }

            recentItemBinding.delete.setOnClickListener {
                recentClick.delete(logModule)
            }

            recentItemBinding.info.setOnClickListener {
                recentClick.info(logModule)
            }

            recentItemBinding.root.setOnLongClickListener {
                selectLog(logModule)
                true
            }

            recentItemBinding.number.text = "Number: ${logModule.number}"
        }

        private fun selectLog(logModule: LogModule) {
            if (SelectLogs.selectLogs.contains(logModule)) {
                SelectLogs.selectLogs.remove(logModule)
                recentItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.transparent))
            } else {
                SelectLogs.selectLogs.add(logModule)
                recentItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.select))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogVH {
        return LogVH(
            LogItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogVH, position: Int) {
        holder.onBind(userLogsList[position], position)
    }

    override fun getItemCount(): Int = userLogsList.size

    interface RecentClick {
        fun call(logModule: LogModule)
        fun message(logModule: LogModule)
        fun delete(logModule: LogModule)
        fun info(logModule: LogModule)
    }

    object SelectLogs {
        var selectLogs = ArrayList<LogModule>()
    }
}