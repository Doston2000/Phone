package uz.lazydevelopers1.callPhone.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.widget.Toast
import uz.lazydevelopers1.callPhone.moduls.ContactModule
import uz.lazydevelopers1.callPhone.moduls.LogModule
import uz.lazydevelopers1.callPhone.ui.contacts.GetContactViewModel
import uz.lazydevelopers1.callPhone.ui.contacts.adapters.ContactAdapter
import uz.lazydevelopers1.callPhone.ui.recents.GetLogViewModel
import uz.lazydevelopers1.callPhone.ui.recents.adapters.LogAdapter


object DeleteLogsAndContacts {

    fun deleteMarkedCallLogs(activity: Activity?, markedCallLogs: ArrayList<LogModule>) {
        try {
            val contentResolver = activity?.contentResolver

            for (log in markedCallLogs) {
                for (id in log.similarLogsIds) {
                    val deletedRows = contentResolver?.delete(
                        CallLog.Calls.CONTENT_URI, CallLog.Calls._ID + "=?", arrayOf(id)
                    )

                    if ((deletedRows ?: 0) > 0) {
                        GetLogViewModel.HoldLogs.logsGroupList.find {
                            it.titleDate == log.ddMMyyyyDate
                        }?.dailyLogs?.removeIf {
                            it.id == id
                        }
                        GetLogViewModel.HoldLogs.logsGroupList.removeIf {
                            it.dailyLogs?.isEmpty() ?: false
                        }
                        GetLogViewModel.HoldLogs.logsGroupLiveData.postValue(GetLogViewModel.HoldLogs.logsGroupList)
                    }
                }
            }
        } catch (_: SecurityException) {
        }
    }

    fun selectedLogsDelete(context: Context, activity: Activity?) {
        val selectedLogs = LogAdapter.SelectLogs.selectLogs
        if (selectedLogs.isNotEmpty()) {
            Dialog(object : Dialog.DialogClick {
                override fun positive() {
                    deleteMarkedCallLogs(
                        activity,
                        selectedLogs
                    )
                }

                override fun negative() {

                }
            }).show(context, "Delete", "delete selected logs ?")
        } else {
            Toast.makeText(
                context,
                "No specified logs available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun selectedContactsDelete(context: Context, activity: Activity?) {
        val selectedContacts = ContactAdapter.SelectContacts.selectContacts
        if (selectedContacts.isNotEmpty()) {
            Dialog(object : Dialog.DialogClick {
                override fun positive() {
                    deleteContacts(
                        activity,
                        selectedContacts
                    )
                }

                override fun negative() {

                }
            }).show(context, "Delete", "delete selected contacts ?")
        } else {
            Toast.makeText(
                context,
                "No specified logs available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("Recycle", "Range")
    fun deleteContacts(activity: Activity?, contacts: ArrayList<ContactModule>) {
        for (contact in contacts) {
            val number =
                if (contact.number?.isNotEmpty() == true) contact.number?.first()
                    ?: "" else ""
            val contactUri =
                Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
            val cur = activity?.contentResolver?.query(contactUri, null, null, null, null)

            try {
                if (cur!!.moveToFirst()) {
                    do {
                        val name = contact.name
                        if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME))
                                .equals(name, ignoreCase = true)
                        ) {
                            val lookupKey =
                                cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
                                )
                            val uri = Uri.withAppendedPath(
                                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                lookupKey
                            )
                            val delete = activity.contentResolver?.delete(uri, null, null)
                            if ((delete ?: 0) > 0) {
                                GetContactViewModel.HoldContacts.contactsGroupList.find {
                                    val firstLetter =
                                        (contact.name ?: "name not found").first().uppercase()
                                    it.title == firstLetter
                                }?.contacts?.removeIf {
                                    it.id == (contact.id ?: 0)
                                }
                                GetContactViewModel.HoldContacts.contactsGroupList.removeIf {
                                    it.contacts?.isEmpty() ?: false
                                }
                                GetContactViewModel.HoldContacts.contactsGroupLiveData.postValue(
                                    GetContactViewModel.HoldContacts.contactsGroupList
                                )
                            }
                        }
                    } while (cur.moveToNext())
                }
            } catch (e: java.lang.Exception) {
                println(e.stackTrace)
            }
        }
    }

}