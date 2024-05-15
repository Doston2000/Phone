package uz.lazydevelopers1.callPhone.utils

import uz.lazydevelopers1.callPhone.moduls.ContactModule
import uz.lazydevelopers1.callPhone.moduls.ContactsGroupModule
import java.util.Locale

class SortContactsData {

    fun sortContacts(contacts: ArrayList<ContactModule>): ArrayList<ContactsGroupModule> {

        val contactsGroupList = ArrayList<ContactsGroupModule>()

        val a = contacts.sortedBy {
            it.name
        }

        contacts.clear()

        contacts.addAll(a)

        // Create a map to hold the separate lists
        val sortedLists = mutableMapOf<Char, MutableList<ContactModule>>()

        // Iterate through the input data and add items to the appropriate lists
        for (item in contacts) {
            val firstLetter = (item.name ?: "name not found").first().lowercaseChar()
            if (!sortedLists.containsKey(firstLetter)) {
                sortedLists[firstLetter] = mutableListOf()
            }
            sortedLists[firstLetter]?.add(item)
        }

        for (sortedList in sortedLists) {
            contactsGroupList.add(
                ContactsGroupModule(
                    sortedList.value as ArrayList,
                    sortedList.key.toString().uppercase(Locale.getDefault())
                )
            )
        }

        return contactsGroupList
    }

}