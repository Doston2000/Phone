package uz.lazydevelopers1.phone.moduls

class ContactModule {

    var id: Long? = null
    var name: String? = null
    var number: ArrayList<String>? = null
    var img: String? = null

    constructor()

    constructor(id: Long?, name: String?, number: ArrayList<String>?, img: String?) {
        this.id = id
        this.name = name
        this.number = number
        this.img = img
    }

}
