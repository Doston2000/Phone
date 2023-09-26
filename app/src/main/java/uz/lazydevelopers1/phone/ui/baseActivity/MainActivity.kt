package uz.lazydevelopers1.phone.ui.baseActivity

import android.content.ContentResolver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.CalendarContract.Calendars.NAME
import android.provider.ContactsContract
import android.provider.ContactsContract.Intents.Insert.PHONE
import android.telephony.TelephonyManager
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.lazydevelopers1.phone.ActivityListener
import uz.lazydevelopers1.phone.FragmentListener
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.ActivityMainBinding
import uz.lazydevelopers1.phone.receivers.CallReceiver
import uz.lazydevelopers1.phone.ui.contacts.GetContactViewModel
import uz.lazydevelopers1.phone.ui.search.SearchActivity
import uz.lazydevelopers1.phone.ui.splashPermissions.PermissionsActivity
import uz.lazydevelopers1.phone.utils.BottomNavigationController
import uz.lazydevelopers1.phone.utils.DeleteLogsAndContacts
import uz.lazydevelopers1.phone.utils.RequestPermission
import uz.lazydevelopers1.phone.utils.SelectFragment

class MainActivity : AppCompatActivity(), FragmentListener {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainPagerAdapter: MainPagerAdapter
    private lateinit var bottomNavigationController: BottomNavigationController
    private var selectFragment = SelectFragment.KEYPAD
    private var phoneNumber = ""
    private var activityResumeListener: ActivityListener? = null
    private val callReceiver = CallReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val intentFilter = IntentFilter()
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        registerReceiver(callReceiver, intentFilter)

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)

        mainBinding.apply {
            viewPager.adapter = mainPagerAdapter

            viewPager.offscreenPageLimit = 2

            viewPager.isUserInputEnabled = false

            bottomNavigationController = BottomNavigationController(
                this@MainActivity,
                keypadText,
                keypadLine,
                recentsText,
                recentsLine,
                contactsText,
                contactsLine,
                keypad,
                recents,
                contacts
            )

            search.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            add.setOnClickListener {
                if (selectFragment == SelectFragment.KEYPAD) {
                    createContact(phoneNumber)
                } else if (selectFragment == SelectFragment.CONTACT) {
                    createContact("")
                }
            }

            more.setOnClickListener { v: View ->
                when (selectFragment) {
                    SelectFragment.KEYPAD -> {
                        showMenu(v, R.menu.keypad_menu)
                    }

                    SelectFragment.LOG -> {
                        showMenu(v, R.menu.log_menu)
                    }

                    SelectFragment.CONTACT -> {
                        showMenu(v, R.menu.contact_menu)
                    }
                }
            }

            keypad.setOnClickListener {
                bottomNavigationController.selectBtn(selectFragment)
                viewPager.setCurrentItem(0, false)
            }

            recents.setOnClickListener {
                bottomNavigationController.selectBtn(selectFragment)
                viewPager.setCurrentItem(1, false)
            }

            contacts.setOnClickListener {
                bottomNavigationController.selectBtn(selectFragment)
                viewPager.setCurrentItem(2, false)
            }
        }

        mainBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        if (phoneNumber.isNotEmpty()) mainBinding.add.visibility =
                            View.VISIBLE else mainBinding.add.visibility = View.INVISIBLE
                        selectFragment = SelectFragment.KEYPAD
                        bottomNavigationController.selectBtn(selectFragment)
                    }

                    1 -> {
                        mainBinding.add.visibility = View.INVISIBLE
                        selectFragment = SelectFragment.LOG
                        bottomNavigationController.selectBtn(selectFragment)
                    }

                    2 -> {
                        mainBinding.add.visibility = View.VISIBLE
                        selectFragment = SelectFragment.CONTACT
                        bottomNavigationController.selectBtn(selectFragment)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private fun createContact(pNumber: String) {
        val i = Intent(Intent.ACTION_INSERT_OR_EDIT)
        i.type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
        i.putExtra(NAME, "")
        i.putExtra(PHONE, pNumber)
        startActivity(i)
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val wrapper = ContextThemeWrapper(this, R.style.YOURSTYLE_PopupMenu)
        val popup = PopupMenu(wrapper, v, 0)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when (menuItem.itemId) {

                //For Keypad
                R.id.speed_dial_numbers_keypad -> {
                    Toast.makeText(this, "Speed dial numbers keypad", Toast.LENGTH_SHORT).show()
                }

                R.id.settings_keypad -> {
                    Toast.makeText(this, "Settings keypad", Toast.LENGTH_SHORT).show()
                }

                //For Log
                R.id.delete_log -> {
//                    Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
                    DeleteLogsAndContacts.selectedLogsDelete(this, this)
                }

                R.id.show_messages -> {
                    Toast.makeText(this, "Show message", Toast.LENGTH_SHORT).show()
                }

                R.id.hide_blocked_calls -> {
                    Toast.makeText(this, "Hide blocked calls", Toast.LENGTH_SHORT).show()
                }

                R.id.total_call_time -> {
                    Toast.makeText(this, "Total call time", Toast.LENGTH_SHORT).show()
                }

                R.id.settings_log -> {
                    Toast.makeText(this, "Settings log", Toast.LENGTH_SHORT).show()
                }

                //For Contact
                R.id.delete_contacts -> {
                    DeleteLogsAndContacts.selectedContactsDelete(this,this)
//                    Toast.makeText(this, "Delete contact", Toast.LENGTH_SHORT).show()
                }

                R.id.share_contacts -> {
                    Toast.makeText(this, "Share contact", Toast.LENGTH_SHORT).show()
                }

                R.id.speed_dial_numbers_contact -> {
                    Toast.makeText(this, "Speed dial numbers contact", Toast.LENGTH_SHORT).show()
                }

                R.id.settings_contact -> {
                    Toast.makeText(this, "Settings contact", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    fun setActivityResumeListener(listener: ActivityListener) {
        activityResumeListener = listener
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) {
            getContact()
        }
//        activityResumeListener?.onActivityResumeForRecent()
//        activityResumeListener?.onActivityResumeForContact()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(callReceiver)
    }

    private fun getContact() {
        if (RequestPermission(applicationContext!!).checkReadContactPermission()) {
            GetContactViewModel(contentResolver).getTodayContacts()
        } else {
            openPermissionActivity()
        }
    }

    override fun openPermissionActivity() {
        val intent = Intent(this, PermissionsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun listenKeypadInput(phoneNumber: String) {
        if (phoneNumber.isNotEmpty() && selectFragment == SelectFragment.KEYPAD) {
            mainBinding.add.visibility = View.VISIBLE
        } else {
            mainBinding.add.visibility = View.INVISIBLE
        }
        this.phoneNumber = phoneNumber
    }

    override fun backPress() {
        if (selectFragment == SelectFragment.KEYPAD) {
            finish()
        } else {
            mainBinding.viewPager.currentItem = 0
            bottomNavigationController.selectBtn(selectFragment)
        }
    }

}