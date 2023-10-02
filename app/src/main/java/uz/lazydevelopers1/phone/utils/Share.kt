package uz.lazydevelopers1.phone.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import uz.lazydevelopers1.phone.moduls.ContactModule
import uz.lazydevelopers1.phone.ui.contacts.adapters.ContactAdapter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object Share {

    fun selectedContactsShare(activity: Activity?) {
        val selectedContacts = ContactAdapter.SelectContacts.selectContacts
        if (selectedContacts.isNotEmpty()) {
            shareContacts(activity, selectedContacts)
        } else {
            Toast.makeText(
                activity?.applicationContext,
                "No specified logs available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun shareContacts(activity: Activity?, contacts: ArrayList<ContactModule>) {
        val csvData = StringBuilder()

        for (contact in contacts) {
            csvData.append(contact).append("\n")
        }

        try {
            val file = File(activity?.getExternalFilesDir(null), "contacts.csv")
            val fileWriter = FileWriter(file)
            fileWriter.write(csvData.toString())
            fileWriter.close()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/csv"
            val fileUri = FileProvider.getUriForFile(
                activity?.applicationContext!!,
                "uz.lazydevelopers1.phone.utils.Share",
                file
            )
            intent.putExtra(Intent.EXTRA_STREAM, fileUri)

            activity.startActivity(Intent.createChooser(intent, "Share contacts"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun shareContact(activity: Activity?, contactModule: ContactModule?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val number = if (contactModule?.number?.isNotEmpty() == true) contactModule.number?.first() else ""
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "$number"
        )
        activity?.startActivity(intent)
    }

}