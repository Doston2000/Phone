package uz.lazydevelopers1.callPhone.moduls

class ContactsGroupModule {

    var contacts: ArrayList<ContactModule>? = null
    var title: String? = null

    constructor()

    constructor(contacts: ArrayList<ContactModule>?, title: String?) {
        this.contacts = contacts
        this.title = title
    }

}