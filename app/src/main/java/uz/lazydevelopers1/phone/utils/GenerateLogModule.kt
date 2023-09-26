package uz.lazydevelopers1.phone.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.CallLog.Calls
import android.provider.ContactsContract
import uz.lazydevelopers1.phone.moduls.LogModule
import java.text.SimpleDateFormat


object GenerateLogModule {

    @SuppressLint("Range")
    fun createLogModule(cursor: Cursor, contentResolver: ContentResolver?): LogModule {
        val id = cursor.getString(cursor.getColumnIndex(Calls._ID))
        val code = cursor.getColumnIndex(Calls.CACHED_NAME)
        val name = if (code < 0) null else cursor.getString(
            cursor.getColumnIndex(Calls.CACHED_NAME)
        )
        val number =
            cursor.getString(cursor.getColumnIndex(Calls.NUMBER))
        val date = cursor.getLong(cursor.getColumnIndex(Calls.DATE))
        val duration =
            cursor.getString(cursor.getColumnIndex(Calls.DURATION))
        val type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE))
        val ddMMyyyyDate = formatting(date, "dd MMMM yyyy")
        val hhMMDate = formatting(date, "HH:mm")
        return LogModule(
            id,
            name,
            number,
            date,
            duration,
            type,
            ddMMyyyyDate,
            hhMMDate,
            getContactImageByNumber(number, contentResolver)
        ).apply { similarLogsIds.add(id) }
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatting(date: Long, format: String): String {
        val simpleDateFormatHHMM = SimpleDateFormat(format)
        return simpleDateFormatHHMM.format(date)
    }

    private fun getContactImageByNumber(
        number: String,
        contentResolver: ContentResolver?
    ): Bitmap? {
        val contactUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val cursor = contentResolver?.query(contactUri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val contactId =
                cursor.getLong(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID) ?: 0)
            val photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
            val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, photoUri)

            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream)
            }
        }

        return null
    }

}