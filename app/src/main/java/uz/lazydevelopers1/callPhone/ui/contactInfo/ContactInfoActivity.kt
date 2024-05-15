package uz.lazydevelopers1.callPhone.ui.contactInfo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.setPadding
import uz.lazydevelopers1.callPhone.R
import uz.lazydevelopers1.callPhone.databinding.ActivityContactInfoBinding
import uz.lazydevelopers1.callPhone.ui.splashPermissions.PermissionsActivity
import uz.lazydevelopers1.callPhone.utils.CommunicationOptions
import uz.lazydevelopers1.callPhone.utils.Edit
import uz.lazydevelopers1.callPhone.utils.RequestPermission
import uz.lazydevelopers1.callPhone.utils.Share


class ContactInfoActivity : AppCompatActivity() {

    private lateinit var contactInfoBinding: ActivityContactInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactInfoBinding = ActivityContactInfoBinding.inflate(layoutInflater)
        setContentView(contactInfoBinding.root)

        contactInfoBinding.apply {
            val name = ContactInfoViewModel.ContactInfo.contact?.name
            val number =
                if (ContactInfoViewModel.ContactInfo.contact?.number?.isNotEmpty() == true) ContactInfoViewModel.ContactInfo.contact?.number?.first() else ""
            val image = ContactInfoViewModel.ContactInfo.contact?.img

            contactName.text = name ?: number ?: ""
            contactNumber.text = number
            if (image != null) {
                contactImg.setImageURI(image.toUri())
            } else {
                contactImg.setImageResource(R.drawable.ic_user)
                contactImg.setPadding(50)
            }

            back.setOnClickListener {
                finish()
            }

            call.setOnClickListener {
                call(number ?: "")
            }

            message.setOnClickListener {
                message(number ?: "")
            }

            contactInfoBinding.share.setOnClickListener {
                Share.shareContact(
                    this@ContactInfoActivity,
                    ContactInfoViewModel.ContactInfo.contact
                )
            }

            contactInfoBinding.edit.setOnClickListener {
                editContact()
            }
        }

    }

    private fun editContact() {
        Edit.ediContact(this)
    }

    private fun call(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            if (RequestPermission(this).checkCallPhonePermission()) {
                CommunicationOptions.call(phoneNumber, this)
            } else {
                val intent = Intent(this, PermissionsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun message(number: String) {
        if (number.isNotEmpty()) {
            CommunicationOptions.message(this, number)
        }
    }
}