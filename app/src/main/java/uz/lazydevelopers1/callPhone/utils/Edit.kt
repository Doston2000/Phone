package uz.lazydevelopers1.callPhone.utils

import android.app.Activity
import android.content.Intent
import uz.lazydevelopers1.callPhone.ui.contactInfo.ContactInfoViewModel

object Edit {

    fun ediContact(activity: Activity) {
        val i = Intent(Intent.ACTION_EDIT)
        i.data = ContactInfoViewModel.ContactInfo.contact?.contactUri
        i.putExtra("finishActivityOnSaveCompleted", true)
        activity.startActivity(i)
    }

}