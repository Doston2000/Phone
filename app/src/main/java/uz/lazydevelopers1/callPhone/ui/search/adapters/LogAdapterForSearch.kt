package uz.lazydevelopers1.callPhone.ui.search.adapters

import android.content.Context
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.callPhone.R
import uz.lazydevelopers1.callPhone.databinding.SearchLogItemBinding
import uz.lazydevelopers1.callPhone.moduls.LogModule

class LogAdapterForSearch(
    private var userLogsList: ArrayList<LogModule>,
    var recentClickForSearch: RecentClickForSearch,
    var context: Context
) : RecyclerView.Adapter<LogAdapterForSearch.LogVHForSearch>() {

    inner class LogVHForSearch(private var searchLogItemBinding: SearchLogItemBinding) :
        RecyclerView.ViewHolder(searchLogItemBinding.root) {
        fun onBind(logModule: LogModule, position: Int) {

            searchLogItemBinding.type.text = when (logModule.type) {
                CallLog.Calls.BLOCKED_TYPE -> {
                    searchLogItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_locked)
                    "Blocked Call"
                }

                CallLog.Calls.INCOMING_TYPE -> {
                    searchLogItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_callback)
                    "Incoming call"
                }

                CallLog.Calls.MISSED_TYPE -> {
                    searchLogItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_missed)
                    "Missed call"
                }

                CallLog.Calls.OUTGOING_TYPE -> {
                    searchLogItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_forwarded)
                    "Outgoing call"
                }

                CallLog.Calls.REJECTED_TYPE -> {
                    searchLogItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone_disabled)
                    "Rejected call"
                }

                else -> {
                    searchLogItemBinding.logStatusIc.setImageResource(R.drawable.ic_phone)
                    "call"
                }

            }

            if (position == 0) {
                searchLogItemBinding.line.visibility = View.INVISIBLE
            } else {
                searchLogItemBinding.line.visibility = View.VISIBLE
            }

            val info =
                if (logModule.name?.isNotEmpty() == true) logModule.name else logModule.number

            searchLogItemBinding.userInfo.text =
                (info) + if (logModule.similarLogsIds.size > 1) " (${logModule.similarLogsIds.size})" else ""

            searchLogItemBinding.logTime.text = logModule.hhMMDate

            searchLogItemBinding.root.setOnClickListener {
                if (searchLogItemBinding.logBottomContainer.visibility == View.GONE) {
                    searchLogItemBinding.logBottomContainer.visibility = View.VISIBLE
                } else if (searchLogItemBinding.logBottomContainer.visibility == View.VISIBLE) {
                    searchLogItemBinding.logBottomContainer.visibility = View.GONE
                }
            }

            searchLogItemBinding.call.setOnClickListener {
                recentClickForSearch.call(logModule)
            }

            searchLogItemBinding.message.setOnClickListener {
                recentClickForSearch.message(logModule)
            }

            searchLogItemBinding.number.text = "Number: ${logModule.number}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogVHForSearch {
        return LogVHForSearch(
            SearchLogItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogVHForSearch, position: Int) {
        holder.onBind(userLogsList[position], position)
    }

    override fun getItemCount(): Int = userLogsList.size

    interface RecentClickForSearch {
        fun call(logModule: LogModule)
        fun message(logModule: LogModule)
    }
}