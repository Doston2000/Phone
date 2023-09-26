package uz.lazydevelopers1.phone.ui.recents

import android.content.ContentResolver
import android.provider.CallLog.Calls
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.lazydevelopers1.phone.moduls.LogModule
import uz.lazydevelopers1.phone.moduls.LogsGroupModule
import uz.lazydevelopers1.phone.utils.GenerateLogModule
import uz.lazydevelopers1.phone.utils.SortLogsData
import java.util.Calendar

class GetLogViewModel(private var contentResolver: ContentResolver?, private var limit: Int) :
    ViewModel() {

    object HoldLogs {
        var logsGroupLiveData = MutableLiveData<ArrayList<LogsGroupModule>?>()
        var logsGroupList = ArrayList<LogsGroupModule>()
    }

    private val projection = arrayOf(
        Calls._ID,
        Calls.CACHED_NAME,
        Calls.NUMBER,
        Calls.DATE,
        Calls.DURATION,
        Calls.TYPE
    )

    fun getTodayLogs() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -4)

        val cursor = contentResolver?.query(
            Calls.CONTENT_URI,
            projection,
            Calls.DATE + " >= ?",
            arrayOf(calendar.timeInMillis.toString()),
            "${Calls.DATE} DESC"
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                val logsList = ArrayList<LogModule>()
                while (cursor.moveToNext()) {
                    logsList.add(GenerateLogModule.createLogModule(cursor,contentResolver))
                }
                val todayLogsList = SortLogsData.sortLogs(logsList)
                todayLogsList.forEach { item ->

                    var findResponse = false

                    item.dailyLogs?.addAll(
                        HoldLogs.logsGroupList.find {
                            if (it.titleDate == item.titleDate) {
                                findResponse = true
                                true
                            } else {
                                findResponse = false
                                false
                            }
                        }?.dailyLogs ?: ArrayList()
                    )

                    val a = item.dailyLogs?.distinctBy {
                        "${it.id}"
                    }?.sortedBy {
                        it.date
                    }?.reversed()

                    item.dailyLogs?.clear()
                    item.dailyLogs?.addAll(sortLogByType(a?.reversed() ?: ArrayList()))

                    if (findResponse) {
                        HoldLogs.logsGroupList.find { it.titleDate == item.titleDate }?.dailyLogs =
                            item.dailyLogs
                    } else {
                        val holdList = ArrayList<LogsGroupModule>()
                        holdList.addAll(HoldLogs.logsGroupList)
                        HoldLogs.logsGroupList.clear()
                        HoldLogs.logsGroupList.add(item)
                        HoldLogs.logsGroupList.addAll(holdList)
                    }
                }
                HoldLogs.logsGroupLiveData.postValue(HoldLogs.logsGroupList)
            } else {
                HoldLogs.logsGroupLiveData.postValue(null)
            }
            cursor.close()
        }
    }

    fun getAllLogs() {
        val cursor = contentResolver?.query(
            Calls.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                val logsList = ArrayList<LogModule>()
                HoldLogs.logsGroupList.clear()
                var count = 0
                while (cursor.moveToNext() && count < limit) {
                    logsList.add(GenerateLogModule.createLogModule(cursor,contentResolver))
                    count++
                }
                HoldLogs.logsGroupList.addAll(SortLogsData.sortLogs(sortLogByType(logsList)))
                HoldLogs.logsGroupLiveData.postValue(HoldLogs.logsGroupList)
            } else {
                HoldLogs.logsGroupLiveData.postValue(null)
            }
            cursor.close()
        }
    }

    private fun sortLogByType(logsList: List<LogModule>): ArrayList<LogModule> {
        val l = ArrayList<LogModule>()
        for (i in logsList.size - 1 downTo 0) {
            val oldLog = logsList[i]
            val newLog = logsList[i - if (i == 0) 0 else 1]
            if ((oldLog.name == newLog.name && oldLog.number == newLog.number && oldLog.ddMMyyyyDate == newLog.ddMMyyyyDate) && ((oldLog.type == newLog.type) || (oldLog.type == Calls.OUTGOING_TYPE && newLog.type == Calls.INCOMING_TYPE) || (newLog.type == Calls.OUTGOING_TYPE && oldLog.type == Calls.INCOMING_TYPE))) {
                if (newLog.id == oldLog.id) {
                    l.add(logsList[i])
                } else {
                    logsList[i - 1].similarLogsIds.addAll(logsList[i].similarLogsIds)
                }
            } else {
                l.add(logsList[i])
            }
        }
        return l
    }

}