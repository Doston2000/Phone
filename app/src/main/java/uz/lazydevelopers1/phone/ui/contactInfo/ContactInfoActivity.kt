package uz.lazydevelopers1.phone.ui.contactInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.setPadding
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.ActivityContactInfoBinding
import uz.lazydevelopers1.phone.databinding.ActivityLogInfoBinding
import uz.lazydevelopers1.phone.ui.logInfo.LogInfoViewModel
import uz.lazydevelopers1.phone.ui.logInfo.adapters.LogInfoGroupAdapter
import uz.lazydevelopers1.phone.ui.splashPermissions.PermissionsActivity
import uz.lazydevelopers1.phone.utils.CommunicationOptions
import uz.lazydevelopers1.phone.utils.RequestPermission

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
        }

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
            if (RequestPermission(this).checkSendSmsPermission()
            ) {
                CommunicationOptions.message(this, number)
            } else {
                val intent = Intent(this, PermissionsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}