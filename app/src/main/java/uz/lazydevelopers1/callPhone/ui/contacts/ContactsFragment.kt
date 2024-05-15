package uz.lazydevelopers1.callPhone.ui.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.lazydevelopers1.callPhone.ActivityListener
import uz.lazydevelopers1.callPhone.FragmentListener
import uz.lazydevelopers1.callPhone.databinding.FragmentContactsBinding
import uz.lazydevelopers1.callPhone.moduls.ContactModule
import uz.lazydevelopers1.callPhone.ui.contacts.adapters.ContactsGroupAdapter
import uz.lazydevelopers1.callPhone.moduls.ContactsGroupModule
import uz.lazydevelopers1.callPhone.ui.baseActivity.MainActivity
import uz.lazydevelopers1.callPhone.ui.contactInfo.ContactInfoActivity
import uz.lazydevelopers1.callPhone.ui.contactInfo.ContactInfoViewModel
import uz.lazydevelopers1.callPhone.ui.contacts.adapters.ContactAdapter
import uz.lazydevelopers1.callPhone.utils.CommunicationOptions
import uz.lazydevelopers1.callPhone.utils.DeleteLogsAndContacts
import uz.lazydevelopers1.callPhone.utils.RequestPermission

class ContactsFragment : Fragment(), ActivityListener {

    private lateinit var contactsBinding: FragmentContactsBinding
    private lateinit var contactsGroupAdapter: ContactsGroupAdapter
    private lateinit var contactsGroupList: ArrayList<ContactsGroupModule>

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (ContactAdapter.SelectContacts.selectContacts.isEmpty()) {
                fragmentListener?.backPress()
            } else {
                ContactAdapter.SelectContacts.selectContacts.clear()
                contactsGroupAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contactsBinding = FragmentContactsBinding.inflate(inflater, container, false)

        (activity as? MainActivity)?.setActivityResumeListener(this)

        GlobalScope.launch(Dispatchers.IO) {
            getContact()
        }

        contactsGroupList = ArrayList()

        contactsGroupAdapter =
            ContactsGroupAdapter(
                activity?.applicationContext!!,
                contactsGroupList,
                object : ContactAdapter.ContactClick {
                    override fun call(contactModule: ContactModule) {
                        call(
                            if (contactModule.number?.isNotEmpty() == true) contactModule.number?.first()
                                ?: "" else ""
                        )
                    }

                    override fun message(contactModule: ContactModule) {
                        message(
                            if (contactModule.number?.isNotEmpty() == true) contactModule.number?.first()
                                ?: "" else ""
                        )
                    }

                    override fun delete(contactModule: ContactModule) {
                        ContactAdapter.SelectContacts.selectContacts.clear()
                        deleteContact(contactModule)
                    }

                    override fun info(contactModule: ContactModule) {
                        contactInfo(contactModule)
                    }
                })
        contactsBinding.rv.adapter = contactsGroupAdapter

        getContacts()

        return contactsBinding.root
    }

    private fun contactInfo(contactModule: ContactModule) {
        ContactInfoViewModel.ContactInfo.contact = contactModule
        val intent = Intent(activity, ContactInfoActivity::class.java)
        startActivity(intent)
    }

    private fun call(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            if (RequestPermission(
                    activity?.applicationContext!!
                ).checkCallPhonePermission()
            ) {
                CommunicationOptions.call(phoneNumber, activity)
            } else {
                fragmentListener?.openPermissionActivity()
            }
        }
    }

    private fun message(number: String) {
        if (number.isNotEmpty()) {
            CommunicationOptions.message(activity, number)
        }
    }

    private fun deleteContact(contactModule: ContactModule) {
        DeleteLogsAndContacts.deleteContacts(
            activity,
            ArrayList<ContactModule>().apply { add(contactModule) })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContacts() {
        contactsBinding.progress.visibility = View.VISIBLE
        GetContactViewModel.HoldContacts.contactsGroupLiveData.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                contactsGroupList.clear()
                contactsGroupList.addAll(it)
                contactsBinding.rv.visibility = View.VISIBLE
                contactsGroupAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Contacts empty", Toast.LENGTH_SHORT).show()
            }
            contactsBinding.progress.visibility = View.GONE
        }
    }

    override fun onActivityResumeForRecent() {

    }

    override fun onActivityResumeForContact() {

    }

    private fun getContact() {
        if (RequestPermission(activity?.applicationContext!!).checkReadContactPermission()) {
            GetContactViewModel(activity?.contentResolver).getAllContacts()
        } else {
            fragmentListener?.openPermissionActivity()
        }
    }

    //----------------------------------
    private var fragmentListener: FragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            fragmentListener = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentListener = null
    }
    //----------------------------------
}