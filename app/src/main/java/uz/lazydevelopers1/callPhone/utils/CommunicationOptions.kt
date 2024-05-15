package uz.lazydevelopers1.callPhone.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object CommunicationOptions {

    fun call(number: String, activity: Activity?) {
        var phoneNumber = number
        if (phoneNumber.startsWith("*") && phoneNumber.endsWith("#")) {
            phoneNumber = phoneNumber.substring(0, phoneNumber.length - 1)
            phoneNumber += Uri.encode("#")
        }
        val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        try {
            activity?.startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                activity?.applicationContext,
                "Call failed, please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun message(activity: Activity?, number: String) {
        val smsUri = Uri.parse("smsto:$number")
        val intent = Intent(Intent.ACTION_SENDTO, smsUri)
        try {
            activity?.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                activity?.applicationContext,
                "SMS failed, please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}