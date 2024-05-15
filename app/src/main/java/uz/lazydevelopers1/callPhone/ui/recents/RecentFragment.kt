package uz.lazydevelopers1.callPhone.ui.recents

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.lazydevelopers1.callPhone.ActivityListener
import uz.lazydevelopers1.callPhone.FragmentListener
import uz.lazydevelopers1.callPhone.databinding.FragmentRecentBinding
import uz.lazydevelopers1.callPhone.moduls.LogModule
import uz.lazydevelopers1.callPhone.moduls.LogsGroupModule
import uz.lazydevelopers1.callPhone.ui.baseActivity.MainActivity
import uz.lazydevelopers1.callPhone.ui.logInfo.LogInfoActivity
import uz.lazydevelopers1.callPhone.ui.logInfo.LogInfoViewModel
import uz.lazydevelopers1.callPhone.ui.recents.adapters.LogAdapter
import uz.lazydevelopers1.callPhone.ui.recents.adapters.LogsGroupAdapter
import uz.lazydevelopers1.callPhone.utils.CommunicationOptions
import uz.lazydevelopers1.callPhone.utils.DeleteLogsAndContacts
import uz.lazydevelopers1.callPhone.utils.RequestPermission

class RecentFragment : Fragment(), ActivityListener {

    private lateinit var recentBinding: FragmentRecentBinding
    private lateinit var logsGroupAdapter: LogsGroupAdapter
    private lateinit var logsGroupList: ArrayList<LogsGroupModule>
    var isLoading = false
    var currentPage = 1


    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (LogAdapter.SelectLogs.selectLogs.isEmpty()) {
                fragmentListener?.backPress()
            } else {
                LogAdapter.SelectLogs.selectLogs.clear()
                logsGroupAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recentBinding = FragmentRecentBinding.inflate(inflater, container, false)

        (activity as? MainActivity)?.setActivityResumeListener(this)

        getLogs()

        logsGroupList = ArrayList()

        val layoutManager = LinearLayoutManager(context)
        recentBinding.rv.layoutManager = layoutManager
        logsGroupAdapter = LogsGroupAdapter(logsGroupList, object : LogAdapter.RecentClick {
            override fun call(logModule: LogModule) {
                call(logModule.number ?: "")
            }

            override fun message(logModule: LogModule) {
                message(logModule.number ?: "")
            }

            override fun delete(logModule: LogModule) {
                LogAdapter.SelectLogs.selectLogs.clear()
                deleteLog(logModule)
            }

            override fun info(logModule: LogModule) {
                logInfo(logModule)
            }
        }, activity?.applicationContext!!)
        recentBinding.rv.adapter = logsGroupAdapter

        recentBinding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && currentPage < 20 && lastVisibleItem == totalItemCount - 1) {
                    getLogs()
                    isLoading = true
                    currentPage++
                }
            }
        })

        getCallLogs()

        return recentBinding.root
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

    private fun deleteLog(logModule: LogModule) {
        DeleteLogsAndContacts.deleteMarkedCallLogs(
            activity,
            ArrayList<LogModule>().apply { add(logModule) })
    }

    private fun logInfo(logModule: LogModule) {
        LogInfoViewModel.LogInfo.log = logModule
        val intent = Intent(activity, LogInfoActivity::class.java)
        activity?.startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCallLogs() {
        recentBinding.progress.visibility = View.VISIBLE
        GetLogViewModel.HoldLogs.logsGroupLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                logsGroupList.clear()
                logsGroupList.addAll(it)
                recentBinding.rv.visibility = View.VISIBLE
                logsGroupAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Logs empty", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
            recentBinding.progress.visibility = View.GONE
        }
    }

    override fun onActivityResumeForRecent() {

    }

    override fun onActivityResumeForContact() {

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getLogs() {
        GlobalScope.launch(Dispatchers.IO) {
            if (RequestPermission(
                    activity?.applicationContext!!
                ).checkReadCallLogPermission()
            ) {
                GetLogViewModel(activity?.contentResolver, currentPage * 100).getAllLogs()
            } else {
                fragmentListener?.openPermissionActivity()
            }
        }
    }

    // --------------------------------
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