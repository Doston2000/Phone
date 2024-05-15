package uz.lazydevelopers1.callPhone.ui.contacts

import android.content.ContentResolver
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.lazydevelopers1.callPhone.moduls.ContactModule
import uz.lazydevelopers1.callPhone.moduls.ContactsGroupModule
import uz.lazydevelopers1.callPhone.utils.GenerateContactModule
import uz.lazydevelopers1.callPhone.utils.SortContactsData
import java.util.Calendar

class GetContactViewModel(
    private var contentResolver: ContentResolver?
) : ViewModel() {

    object HoldContacts {
        var contactsGroupLiveData = MutableLiveData<ArrayList<ContactsGroupModule>?>()
        var contactsGroupList = ArrayList<ContactsGroupModule>()
        var contactsList = ArrayList<ContactModule>()
    }

    private val projection =
        arrayOf(
            Phone._ID,
            Phone.DISPLAY_NAME,
        )

    fun getTodayContacts() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -4)

        val cursor = contentResolver?.query(
            Phone.CONTENT_URI,
            projection,
            Phone.CONTACT_LAST_UPDATED_TIMESTAMP + " >= ?",
            arrayOf(calendar.timeInMillis.toString()),
            Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                val contacts = ArrayList<ContactModule>()
                while (cursor.moveToNext()) {
                    val contact = GenerateContactModule.createContactModule(cursor, contentResolver)
                    if (contact.name == null) continue
                    contacts.add(contact)
                }

                for (i in 0 until contacts.size) {
                    HoldContacts.contactsList.removeIf { it.contactUri == contacts[i].contactUri }
                }

                HoldContacts.contactsList.addAll(contacts)
                val holdList = HoldContacts.contactsList.distinctBy { "${it.contactUri}" }
                HoldContacts.contactsList.clear()
                HoldContacts.contactsList.addAll(holdList)

                HoldContacts.contactsGroupList.clear()
                HoldContacts.contactsGroupList.addAll(
                    SortContactsData().sortContacts(
                        HoldContacts.contactsList
                    )
                )

                HoldContacts.contactsGroupLiveData.postValue(HoldContacts.contactsGroupList)
            }
            cursor.close()
        }
    }

    fun getAllContacts() {
        val cursor = contentResolver?.query(
            Phone.CONTENT_URI,
            projection,
            null,
            null,
            Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                val contacts = ArrayList<ContactModule>()
                while (cursor.moveToNext()) {
                    val contact = GenerateContactModule.createContactModule(cursor, contentResolver)
                    if (contact.name == null) continue
                    contacts.add(contact)
                }

                HoldContacts.contactsList.clear()
                HoldContacts.contactsList.addAll(contacts)
                val holdList = HoldContacts.contactsList.distinctBy { "${it.name}${it.number}" }
                HoldContacts.contactsList.clear()
                HoldContacts.contactsList.addAll(holdList)

                HoldContacts.contactsGroupList.clear()
                HoldContacts.contactsGroupList.addAll(
                    SortContactsData().sortContacts(
                        HoldContacts.contactsList
                    )
                )

                HoldContacts.contactsGroupLiveData.postValue(HoldContacts.contactsGroupList)
            } else {
                HoldContacts.contactsGroupLiveData.postValue(null)
                HoldContacts.contactsList.clear()
            }
            cursor.close()
        }
    }
}