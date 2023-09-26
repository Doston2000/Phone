package uz.lazydevelopers1.phone.utils

import uz.lazydevelopers1.phone.moduls.LogModule
import uz.lazydevelopers1.phone.moduls.LogsGroupModule
import java.text.SimpleDateFormat

object SortLogsData {

    fun sortLogs(logsList: ArrayList<LogModule>): ArrayList<LogsGroupModule> {

        val logsGroupList = ArrayList<LogsGroupModule>()

        val list = logsList.sortedBy {
            it.date
        }.reversed()

        logsList.clear()

        logsList.addAll(list)

        val dates = ArrayList<String>()

        for (i in 0 until logsList.size) {
            dates.add(logsList[i].ddMMyyyyDate ?: "")
        }

        for (dateDistinct in dates.distinct()) {
            logsGroupList.add(LogsGroupModule(ArrayList(), dateDistinct))
        }

        for (logsGroupModule in logsGroupList) {
            logsGroupModule.dailyLogs?.addAll(logsList.filter { log ->
                log.ddMMyyyyDate == logsGroupModule.titleDate
            })

//            var a = logsGroupModule.dailyLogs?.distinctBy {
//                "${it.type}${it.number}"
//            }
//
//            logsGroupModule.dailyLogs?.clear()
//            logsGroupModule.dailyLogs?.addAll(a!!)

        }

        return logsGroupList
    }

}