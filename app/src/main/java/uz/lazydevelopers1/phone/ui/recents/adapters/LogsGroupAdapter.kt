package uz.lazydevelopers1.phone.ui.recents.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.phone.databinding.LogsGroupItemBinding
import uz.lazydevelopers1.phone.moduls.LogModule
import uz.lazydevelopers1.phone.moduls.LogsGroupModule

class LogsGroupAdapter(
    private var logsGroupsList: ArrayList<LogsGroupModule>,
    var logClick: LogAdapter.RecentClick,
    var context: Context
) :
    RecyclerView.Adapter<LogsGroupAdapter.LogsGroupVH>() {

    inner class LogsGroupVH(private var recentGroupItem: LogsGroupItemBinding) :
        RecyclerView.ViewHolder(recentGroupItem.root) {
        fun onBind(logsGroupModule: LogsGroupModule) {
            recentGroupItem.groupDate.text = logsGroupModule.titleDate
            val logAdapter =
                LogAdapter(logsGroupModule.dailyLogs!!, object : LogAdapter.RecentClick {
                    override fun call(logModule: LogModule) {
                        logClick.call(logModule)
                    }

                    override fun message(logModule: LogModule) {
                        logClick.message(logModule)
                    }

                    override fun delete(logModule: LogModule) {
                        logClick.delete(logModule)
                    }

                    override fun info(logModule: LogModule) {
                        logClick.info(logModule)
                    }
                }, context)
            recentGroupItem.logRv.adapter = logAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsGroupVH {
        return LogsGroupVH(
            LogsGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogsGroupVH, position: Int) {
        holder.onBind(logsGroupsList[position])
    }

    override fun getItemCount(): Int {
        return logsGroupsList.size
    }

}