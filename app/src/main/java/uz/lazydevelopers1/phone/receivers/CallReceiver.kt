package uz.lazydevelopers1.phone.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import uz.lazydevelopers1.phone.FragmentListener
import uz.lazydevelopers1.phone.ui.recents.GetLogViewModel
import uz.lazydevelopers1.phone.utils.RequestPermission

class CallReceiver(private var listener: FragmentListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            // Handle call state changes here
            when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Phone is ringing
                }

                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Call is in progress
                }

                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Call is ended
                    Handler(Looper.myLooper()!!).postDelayed({
                        if (RequestPermission(context!!).checkReadCallLogPermission()) {
                            GetLogViewModel(context.contentResolver, 0).getTodayLogs()
                        } else {
                            listener.openPermissionActivity()
                        }
                    }, 1000)
                }
            }
        }
    }
}