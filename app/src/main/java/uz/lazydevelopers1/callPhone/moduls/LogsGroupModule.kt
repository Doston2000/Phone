package uz.lazydevelopers1.callPhone.moduls

class LogsGroupModule {

    var dailyLogs: ArrayList<LogModule>? = null
    var titleDate: String? = null

    constructor()

    constructor(dailyLogs: ArrayList<LogModule>?, titleDate: String?) {
        this.dailyLogs = dailyLogs
        this.titleDate = titleDate
    }

}