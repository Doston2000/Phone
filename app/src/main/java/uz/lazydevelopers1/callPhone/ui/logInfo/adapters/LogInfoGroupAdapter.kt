package uz.lazydevelopers1.callPhone.ui.logInfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.callPhone.databinding.LogsGroupItemBinding
import uz.lazydevelopers1.callPhone.moduls.LogsGroupModule

class LogInfoGroupAdapter(
    private var logsGroupsList: ArrayList<LogsGroupModule>,
    var context: Context
) : RecyclerView.Adapter<LogInfoGroupAdapter.LogInfoGroupVH>() {

    inner class LogInfoGroupVH(private var recentGroupItem: LogsGroupItemBinding) :
        RecyclerView.ViewHolder(recentGroupItem.root) {
        fun onBind(logsGroupModule: LogsGroupModule) {
            recentGroupItem.groupDate.text = logsGroupModule.titleDate
            val logInfoAdapter = LogInfoAdapter(logsGroupModule.dailyLogs ?: ArrayList())
            recentGroupItem.logRv.adapter = logInfoAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogInfoGroupVH {
        return LogInfoGroupVH(
            LogsGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogInfoGroupVH, position: Int) {
        holder.onBind(logsGroupsList[position])
    }

    override fun getItemCount(): Int {
        return logsGroupsList.size
    }

}