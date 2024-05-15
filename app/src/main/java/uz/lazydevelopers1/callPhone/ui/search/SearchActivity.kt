package uz.lazydevelopers1.callPhone.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.lazydevelopers1.callPhone.databinding.ActivitySearchBinding
import uz.lazydevelopers1.callPhone.moduls.ContactModule
import uz.lazydevelopers1.callPhone.moduls.LogModule
import uz.lazydevelopers1.callPhone.ui.search.adapters.ContactAdapterForSearch
import uz.lazydevelopers1.callPhone.ui.search.adapters.LogAdapterForSearch
import uz.lazydevelopers1.callPhone.ui.splashPermissions.PermissionsActivity
import uz.lazydevelopers1.callPhone.utils.CommunicationOptions
import uz.lazydevelopers1.callPhone.utils.RequestPermission
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding
    private var handler = Handler(Looper.myLooper()!!)
    private lateinit var logAdapterForSearch: LogAdapterForSearch
    private lateinit var contactAdapterForSearch: ContactAdapterForSearch
    private var contacts = ArrayList<ContactModule>()
    private var logs = ArrayList<LogModule>()
    private var RECUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        contactAdapterForSearch = ContactAdapterForSearch(
            this,
            contacts,
            object : ContactAdapterForSearch.ContactClickForSearch {
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
            })
        logAdapterForSearch =
            LogAdapterForSearch(logs, object : LogAdapterForSearch.RecentClickForSearch {
                override fun call(logModule: LogModule) {
                    call(logModule.number ?: "")
                }

                override fun message(logModule: LogModule) {
                    message(logModule.number ?: "")
                }
            }, this)

        searchBinding.apply {

            contactRv.adapter = contactAdapterForSearch
            logRv.adapter = logAdapterForSearch

            contactRv.setOnTouchListener { _, _ ->
                closedKeyboard()
                false
            }

            logRv.setOnTouchListener { _, _ ->
                closedKeyboard()
                false
            }

            searchInput.requestFocus()

            back.setOnClickListener { finish() }

            searchInput.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                closedKeyboard()
                searchInput.clearFocus()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return@OnEditorActionListener true
                }
                true
            })

            searchInput.addTextChangedListener {
                handler.removeCallbacks(run)
                clearAdapters()
                if (searchInput.text.toString() != "") {
                    handler.postDelayed(run, 500)
                }
            }

            mic.setOnClickListener {
                listen()
            }
        }

        generateLogsAndContactsList()

    }

    private fun call(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            if (RequestPermission(
                    applicationContext!!
                ).checkCallPhonePermission()
            ) {
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

    @OptIn(DelicateCoroutinesApi::class)
    private var run = Runnable {
        searchBinding.progress.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            search(searchBinding.searchInput.text.toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun generateLogsAndContactsList() {

        SearchViewModel.HoldSearchItems.contactsLiveData.observe(this) { contacts ->
            searchBinding.progress.visibility = View.INVISIBLE
            if (contacts != null) {
                this.contacts.clear()
                this.contacts.addAll(contacts)
                contactAdapterForSearch.notifyDataSetChanged()
            }
        }

        SearchViewModel.HoldSearchItems.logsLiveData.observe(this) { logs ->
            searchBinding.progress.visibility = View.INVISIBLE
            if (logs != null) {
                this.logs.clear()
                this.logs.addAll(logs)
                logAdapterForSearch.notifyDataSetChanged()
            }
        }
    }

    private fun search(str: String) {
        SearchViewModel(contentResolver).search(str)
    }

    override fun onPause() {
        super.onPause()
        closedKeyboard()
        handler.removeCallbacks(run)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAdapters()
        SearchViewModel(contentResolver).clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearAdapters() {
        contacts.clear()
        logs.clear()
        contactAdapterForSearch.notifyDataSetChanged()
        logAdapterForSearch.notifyDataSetChanged()
    }

    private fun closedKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "")

        try {
            startActivityForResult(intent, RECUEST_CODE)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                searchBinding.searchInput.setText(if (result != null) result[0] else "")
            }
        }
    }
}