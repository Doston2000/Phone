package uz.lazydevelopers1.phone.ui.logInfo

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.lazydevelopers1.phone.moduls.ContactModule
import uz.lazydevelopers1.phone.moduls.LogModule
import uz.lazydevelopers1.phone.moduls.LogsGroupModule
import uz.lazydevelopers1.phone.utils.GenerateLogModule
import uz.lazydevelopers1.phone.utils.SortLogsData


class LogInfoViewModel(private var contentResolver: ContentResolver) : ViewModel() {

    object LogInfo {
        var log: LogModule? = null
        var logHistoryList = MutableLiveData<ArrayList<LogsGroupModule>?>()
    }


    private val projection = arrayOf(
        CallLog.Calls._ID,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.NUMBER,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE
    )

    @OptIn(DelicateCoroutinesApi::class)
    fun getHistory(number: String, all: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            loadLogHistory(number, all)
        }
    }

    fun clearLogInfo() {
        LogInfo.logHistoryList.postValue(null)
    }

    private fun loadLogHistory(number: String, all: Boolean) {
        val selection =
            "${CallLog.Calls.NUMBER} LIKE ? OR ${CallLog.Calls.CACHED_NAME} LIKE ?"

        val selectionArgs = arrayOf("%$number%", "%$number%")

        val cursorForLog: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${CallLog.Calls.DATE} DESC"
        )

        if (cursorForLog != null) {
            if (cursorForLog.count > 0) {
                val logsList = ArrayList<LogModule>()
                var count = 20
                while (cursorForLog.moveToNext()) {
                    if (!all && count <= 0) break
                    logsList.add(GenerateLogModule.createLogModule(cursorForLog, contentResolver))
                    if (!all) count--
                }
                LogInfo.logHistoryList.postValue(SortLogsData.sortLogs(logsList))
            } else {
                LogInfo.logHistoryList.postValue(null)
            }
            cursorForLog.close()
        }
    }

//    fun isContactFavorite(phoneNumber: String?): Boolean {
//        val uri = Uri.withAppendedPath(
//            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
//            Uri.encode(phoneNumber)
//        )
//        val projection = arrayOf(ContactsContract.PhoneLookup.STARRED)
//        val cursor = contentResolver.query(uri, projection, null, null, null)
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                val isFavorite =
//                    cursor.getInt(cursor.getColumnIndex(ContactsContract.PhoneLookup.STARRED) ?: 0)
//                cursor.close()
//                return isFavorite == 1
//            }
//            cursor.close()
//        }
//
//        return false
//    }
//
//    fun getContactByPhoneNumber(context: Context, phoneNumber: String?): ContactModule? {
//        val contentResolver = context.contentResolver
//        val uri = Uri.withAppendedPath(
//            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
//            Uri.encode(phoneNumber)
//        )
//        val projection = arrayOf(
//            ContactsContract.PhoneLookup.DISPLAY_NAME,
//            ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI,
//            ContactsContract.PhoneLookup._ID
//        )
//        val cursor = contentResolver.query(uri, projection, null, null, null)
//        if (cursor != null && cursor.moveToFirst()) {
//            val contactName =
//                cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
//            val contactPhotoUri =
//                cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI))
//            val contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID))
//            cursor.close()
//            return ContactModule(contactId, contactName, contactPhotoUri)
//        }
//        return null
//    }
}