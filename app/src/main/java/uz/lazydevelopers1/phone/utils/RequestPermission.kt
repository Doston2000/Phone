package uz.lazydevelopers1.phone.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RequestPermission(var context: Context) {

    var REQUEST_CALLPHONE = 1
    var REQUEST_READCALLLOG = 2
    var REQUEST_READCONTACTS = 3
    var REQUEST_LISTEN_CALLS = 4
    var REQUEST_SEND_SMS = 5
    var REQUEST_WRITECALLLOG = 6
    var WRITE_CONTACTS = 7

    fun getCallPermission() {
        if (!checkCallPhonePermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.CALL_PHONE,
                ),
                REQUEST_CALLPHONE
            )
        }
    }

    fun getReadCallLogPermission(){
        if (!checkReadCallLogPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.READ_CALL_LOG
                ),
                REQUEST_READCALLLOG
            )
        }
    }

    fun getReadContactPermission(){
        if (!checkReadContactPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.READ_CONTACTS
                ),
                REQUEST_READCONTACTS
            )
        }
    }

    fun getListenCallsPermission(){
        if (!checkListenCallsPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE
                ),
                REQUEST_LISTEN_CALLS
            )
        }
    }

    fun getSendSmsPermission(){
        if (!checkSendSmsPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.SEND_SMS
                ),
                REQUEST_SEND_SMS
            )
        }
    }

    fun getWriteCallLogPermission(){
        if (!checkWriteCallLogPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.WRITE_CALL_LOG
                ),
                REQUEST_WRITECALLLOG
            )
        }
    }

    fun getWriteContactPermission(){
        if (!checkWriteContactPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.WRITE_CONTACTS
                ),
                WRITE_CONTACTS
            )
        }
    }

    fun checkCallPhonePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkReadCallLogPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkReadContactPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkListenCallsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkSendSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkWriteCallLogPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkWriteContactPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

//    fun requestCallPhonePermission() {
//        fragment?.requestPermissions(
//            arrayOf(android.Manifest.permission.CALL_PHONE),
//            REQUEST_CALLPHONE
//        )
//    }
//
//    fun requestReadCallLogPermission() {
//        fragment?.requestPermissions(
//            arrayOf(android.Manifest.permission.READ_CALL_LOG),
//            REQUEST_READCALLLOG
//        )
//    }
//
//    fun requestReadContactPermission() {
//        fragment?.requestPermissions(
//            arrayOf(android.Manifest.permission.READ_CONTACTS),
//            REQUEST_READCONTACTS
//        )
//    }

    fun openPermissionSettings(activity: Activity?) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        activity?.startActivity(intent)
        Toast.makeText(context, "Give Permission", Toast.LENGTH_SHORT).show()
    }

}