package uz.lazydevelopers1.callPhone.moduls

import android.graphics.Bitmap

class LogModule {

    var id: String? = null
    var name: String? = null
    var number: String? = null
    var date: Long? = null
    var duration: String? = null
    var type: Int? = null
    var ddMMyyyyDate: String? = null
    var hhMMDate: String? = null
    var img: Bitmap? = null
    var similarLogsIds = ArrayList<String>()

    constructor()

    constructor(
        id: String?,
        name: String?,
        number: String?,
        date: Long?,
        duration: String?,
        type: Int?,
        ddMMyyyyDate: String?,
        hhMMDate: String?,
        img: Bitmap?,
    ) {
        this.id = id
        this.name = name
        this.number = number
        this.date = date
        this.duration = duration
        this.type = type
        this.ddMMyyyyDate = ddMMyyyyDate
        this.hhMMDate = hhMMDate
        this.img = img
    }

}
