package uz.lazydevelopers1.callPhone.ui.search.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevelopers1.callPhone.databinding.SearchContactItemBinding
import uz.lazydevelopers1.callPhone.moduls.ContactModule
import java.util.Locale

class ContactAdapterForSearch(
    var context: Context,
    private var contactsList: ArrayList<ContactModule>,
    private var contactClickForSearch: ContactClickForSearch
) :
    RecyclerView.Adapter<ContactAdapterForSearch.ContactVHForSearch>() {

    inner class ContactVHForSearch(private var searchContactItemBinding: SearchContactItemBinding) :
        RecyclerView.ViewHolder(searchContactItemBinding.root) {
        fun onBind(contactModule: ContactModule, position: Int) {

            if (position == 0) {
                searchContactItemBinding.line.visibility = View.INVISIBLE
            } else {
                searchContactItemBinding.line.visibility = View.VISIBLE
            }

            searchContactItemBinding.tv.text =
                contactModule.name?.first().toString().uppercase(Locale.getDefault())

            searchContactItemBinding.contactIc.setImageURI(Uri.parse(contactModule.img ?: ""))

            val contactName = contactModule.name
            val contactNumber = contactModule.number
            searchContactItemBinding.userInfo.text = contactName
                ?: if (contactNumber?.isNotEmpty() == true) contactNumber.first() else ""

            searchContactItemBinding.item.setOnClickListener {
                if (searchContactItemBinding.contactBottomContainer.visibility == View.GONE) {
                    searchContactItemBinding.contactBottomContainer.visibility = View.VISIBLE
                } else if (searchContactItemBinding.contactBottomContainer.visibility == View.VISIBLE) {
                    searchContactItemBinding.contactBottomContainer.visibility = View.GONE
                }
            }

            searchContactItemBinding.call.setOnClickListener {
                contactClickForSearch.call(contactModule)
            }

            searchContactItemBinding.message.setOnClickListener {
                contactClickForSearch.message(contactModule)
            }

            searchContactItemBinding.number.text = "Number: ${
                if (contactModule.number?.isNotEmpty() == true) contactModule.number?.first()
                    ?: "" else ""
            }"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVHForSearch {
        return ContactVHForSearch(
            SearchContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactVHForSearch, position: Int) {
        holder.onBind(contactsList[position], position)
    }

    override fun getItemCount(): Int = contactsList.size

    interface ContactClickForSearch {
        fun call(contactModule: ContactModule)
        fun message(contactModule: ContactModule)
    }
}