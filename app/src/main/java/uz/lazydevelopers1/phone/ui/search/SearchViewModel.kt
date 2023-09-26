package uz.lazydevelopers1.phone.ui.search

import android.content.ContentResolver
import android.database.Cursor
import android.provider.CallLog
import androidx.lifecycle.MutableLiveData
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.lifecycle.ViewModel
import uz.lazydevelopers1.phone.moduls.ContactModule
import uz.lazydevelopers1.phone.moduls.LogModule
import uz.lazydevelopers1.phone.utils.GenerateContactModule
import uz.lazydevelopers1.phone.utils.GenerateLogModule

class SearchViewModel(private var contentResolver: ContentResolver) : ViewModel() {

    object HoldSearchItems {
        var logsLiveData = MutableLiveData<ArrayList<LogModule>?>()
        var contactsLiveData = MutableLiveData<ArrayList<ContactModule>?>()
    }

    fun clear() {
        HoldSearchItems.logsLiveData.postValue(null)
        HoldSearchItems.contactsLiveData.postValue(null)
    }

    private val projectionForLog = arrayOf(
        CallLog.Calls._ID,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.NUMBER,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE
    )

    private val projectionForContact =
        arrayOf(
            Phone._ID,
            Phone.DISPLAY_NAME,
            Phone.NUMBER
        )

    fun search(searchStr: String) {

        val selectionForLog =
            "${CallLog.Calls.NUMBER} LIKE ? OR ${CallLog.Calls.CACHED_NAME} LIKE ?"

        val selectionForContact =
            "${Phone.NUMBER} LIKE ? OR ${Phone.DISPLAY_NAME} LIKE ?"

        val selectionArgs = arrayOf("%$searchStr%", "%$searchStr%")

        val cursorForLog: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projectionForLog,
            selectionForLog,
            selectionArgs,
            "${CallLog.Calls.DATE} DESC"
        )

        val cursorForContact = contentResolver.query(
            Phone.CONTENT_URI,
            projectionForContact,
            selectionForContact,
            selectionArgs,
            Phone.DISPLAY_NAME + " ASC"
        )

        if (cursorForLog != null) {
            if (cursorForLog.count > 0) {
                val logsList = ArrayList<LogModule>()
                while (cursorForLog.moveToNext()) {
                    logsList.add(GenerateLogModule.createLogModule(cursorForLog,contentResolver))
                }
                HoldSearchItems.logsLiveData.postValue(logsList)
            } else {
                HoldSearchItems.logsLiveData.postValue(null)
            }
            cursorForLog.close()
        }

        if (cursorForContact != null) {
            if (cursorForContact.count > 0) {
                val contacts = ArrayList<ContactModule>()
                while (cursorForContact.moveToNext()) {
                    val contact = GenerateContactModule.createContactModule(
                        cursorForContact,
                        contentResolver
                    )
                    if (contact.name == null) continue
                    contacts.add(contact)
                }

                HoldSearchItems.contactsLiveData.postValue(contacts)
            } else {
                HoldSearchItems.contactsLiveData.postValue(null)
            }
            cursorForContact.close()
        }
    }

}