package uz.lazydevelopers1.phone.ui.contacts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.phone.databinding.ContactsGroupItemBinding
import uz.lazydevelopers1.phone.moduls.ContactModule
import uz.lazydevelopers1.phone.moduls.ContactsGroupModule

class ContactsGroupAdapter(
    var context: Context,
    private var contactsGroupsList: ArrayList<ContactsGroupModule>,
    var contactClick: ContactAdapter.ContactClick
) :
    RecyclerView.Adapter<ContactsGroupAdapter.ContactsGroupVH>() {

    inner class ContactsGroupVH(private var contactGroupItem: ContactsGroupItemBinding) :
        RecyclerView.ViewHolder(contactGroupItem.root) {
        fun onBind(contactsGroupModule: ContactsGroupModule) {
            contactGroupItem.title.text = contactsGroupModule.title.toString()
            val contactAdapter =
                ContactAdapter(
                    context,
                    contactsGroupModule.contacts!!,
                    object : ContactAdapter.ContactClick {
                        override fun call(contactModule: ContactModule) {
                            contactClick.call(contactModule)
                        }

                        override fun message(contactModule: ContactModule) {
                            contactClick.message(contactModule)
                        }

                        override fun delete(contactModule: ContactModule) {
                            contactClick.delete(contactModule)
                        }

                        override fun info(contactModule: ContactModule) {
                            contactClick.info(contactModule)
                        }
                    })
            contactGroupItem.contactRv.adapter = contactAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsGroupVH {
        return ContactsGroupVH(
            ContactsGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactsGroupVH, position: Int) {
        holder.onBind(contactsGroupsList[position])
    }

    override fun getItemCount(): Int {
        return contactsGroupsList.size
    }

}