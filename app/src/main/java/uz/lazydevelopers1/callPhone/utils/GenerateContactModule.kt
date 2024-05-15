package uz.lazydevelopers1.callPhone.utils

import android.annotation.SuppressLint
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import uz.lazydevelopers1.callPhone.moduls.ContactModule

object GenerateContactModule {

    @SuppressLint("Range", "Recycle")
    fun createContactModule(
        cursor: Cursor,
        contentResolver: ContentResolver?
    ): ContactModule {
        val id: Long =
            cursor.getLong(
                cursor.getColumnIndex(Phone._ID)
            )
        val name: String? =
            cursor.getString(
                cursor.getColumnIndex(Phone.DISPLAY_NAME)
            )

        return ContactModule(
            id,
            name,
            contactUri(contentResolver, id),
            getContactNumber(id, contentResolver),
            getContactsDetails(id, contentResolver)
        )
    }

    @SuppressLint("Range")
    private fun contactUri(contentResolver: ContentResolver?, id: Long): Uri? {

        val selection =
            "${Phone._ID} LIKE ?"

        val selectionArgs = arrayOf("%$id%")

        val uri = Phone.CONTENT_URI
        val cursor = contentResolver?.query(
            uri,
            null,
            selection,
            selectionArgs,
            Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor?.moveToFirst()!!) {
            val idContact: Long =
                cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID))
            return ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContact)
        }
        return null

    }

    @SuppressLint("Range")
    private fun getContactNumber(
        contactId: Long,
        contentResolver: ContentResolver?
    ): ArrayList<String> {
        val phoneCursor = contentResolver?.query(
            Phone.CONTENT_URI,
            null,
            Phone._ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        val numbers = ArrayList<String>()

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                val phoneNumber =
                    phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER))
                numbers.add(phoneNumber)
            }
            phoneCursor.close()
        }
        return numbers
    }

    private fun getContactsDetails(id: Long, contentResolver: ContentResolver?): String? {
        var photo: String? = null
        val contactUri = Uri.withAppendedPath(Phone.CONTENT_URI, id.toString())

        // Projection for the query
        val projection = arrayOf(Phone.PHOTO_URI)

        // Query the contact's photo URI
        val cursor = contentResolver?.query(contactUri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val photoUriColumnIndex = cursor.getColumnIndex(Phone.PHOTO_URI)
            val photoUriString = cursor.getString(photoUriColumnIndex)

            if (photoUriString != null) {
                photo = photoUriString
            }

            cursor.close()
        }
        return photo
    }

}