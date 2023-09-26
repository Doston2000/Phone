package uz.lazydevelopers1.phone.ui.contacts.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.ContactItemsBinding
import uz.lazydevelopers1.phone.moduls.ContactModule
import uz.lazydevelopers1.phone.moduls.LogModule
import uz.lazydevelopers1.phone.ui.recents.adapters.LogAdapter
import java.util.Locale

class ContactAdapter(
    var context: Context,
    private var contactsList: ArrayList<ContactModule>,
    private var contactClick: ContactClick
) :
    RecyclerView.Adapter<ContactAdapter.ContactVH>() {

    inner class ContactVH(private var contactItemBinding: ContactItemsBinding) :
        RecyclerView.ViewHolder(contactItemBinding.root) {
        fun onBind(contactModule: ContactModule, position: Int) {

            if (SelectContacts.selectContacts.contains(contactModule)) {
                contactItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.select))
            } else {
                contactItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.transparent))
            }

            if (position == 0) {
                contactItemBinding.line.visibility = View.INVISIBLE
            } else {
                contactItemBinding.line.visibility = View.VISIBLE
            }

            contactItemBinding.tv.text =
                contactModule.name?.first().toString().uppercase(Locale.getDefault())
            contactItemBinding.contactIc.setImageURI(Uri.parse(contactModule.img ?: ""))
            val contactName = contactModule.name
            val contactNumber = contactModule.number
            contactItemBinding.userInfo.text = contactName
                ?: if (contactNumber?.isNotEmpty() == true) contactNumber.first() else ""

            contactItemBinding.item.setOnClickListener {
                if (SelectContacts.selectContacts.size < 1) {
                    if (contactItemBinding.contactBottomContainer.visibility == View.GONE) {
                        contactItemBinding.contactBottomContainer.visibility = View.VISIBLE
                    } else if (contactItemBinding.contactBottomContainer.visibility == View.VISIBLE) {
                        contactItemBinding.contactBottomContainer.visibility = View.GONE
                    }
                } else {
                    selectContact(contactModule)
                }
            }

            contactItemBinding.item.setOnLongClickListener {
                selectContact(contactModule)
                true
            }

            contactItemBinding.call.setOnClickListener {
                contactClick.call(contactModule)
            }

            contactItemBinding.message.setOnClickListener {
                contactClick.message(contactModule)
            }

            contactItemBinding.delete.setOnClickListener {
                contactClick.delete(contactModule)
            }

            contactItemBinding.info.setOnClickListener {
                contactClick.info(contactModule)
            }

            contactItemBinding.number.text = "Number: ${
                if (contactModule.number?.isNotEmpty() == true) contactModule.number?.first()
                    ?: "" else ""
            }"

        }

        private fun selectContact(contactModule: ContactModule) {
            if (SelectContacts.selectContacts.contains(contactModule)) {
                SelectContacts.selectContacts.remove(contactModule)
                contactItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.transparent))
            } else {
                SelectContacts.selectContacts.add(contactModule)
                contactItemBinding.item.setBackgroundColor(context.resources.getColor(R.color.select))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVH {
        return ContactVH(
            ContactItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactVH, position: Int) {
        holder.onBind(contactsList[position], position)
    }

    override fun getItemCount(): Int = contactsList.size

    interface ContactClick {
        fun call(contactModule: ContactModule)
        fun message(contactModule: ContactModule)
        fun delete(contactModule: ContactModule)
        fun info(contactModule: ContactModule)
    }

    object SelectContacts {
        var selectContacts = ArrayList<ContactModule>()
    }

}