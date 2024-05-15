package uz.lazydevelopers1.callPhone.moduls

import android.net.Uri

class ContactModule {

    var id: Long? = null
    var name: String? = null
    var contactUri: Uri? = null
    var number: ArrayList<String>? = null
    var img: String? = null

    constructor()

    constructor(
        id: Long?,
        name: String?,
        contactUri: Uri?,
        number: ArrayList<String>?,
        img: String?
    ) {
        this.id = id
        this.name = name
        this.contactUri = contactUri
        this.number = number
        this.img = img
    }

}
