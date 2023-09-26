package uz.lazydevelopers1.phone.ui.baseActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.lazydevelopers1.phone.ui.contacts.ContactsFragment
import uz.lazydevelopers1.phone.ui.keypad.KeypadFragment
import uz.lazydevelopers1.phone.ui.recents.RecentFragment

class MainPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KeypadFragment()
            1 -> RecentFragment()
            2 -> ContactsFragment()
            else -> KeypadFragment()
        }
    }
}