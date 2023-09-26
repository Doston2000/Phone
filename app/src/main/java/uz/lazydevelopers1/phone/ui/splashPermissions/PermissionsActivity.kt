package uz.lazydevelopers1.phone.ui.splashPermissions

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.ActivityPermissionsBinding
import uz.lazydevelopers1.phone.ui.baseActivity.MainActivity
import uz.lazydevelopers1.phone.utils.RequestPermission

class PermissionsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var permissionsBinding: ActivityPermissionsBinding
    lateinit var requestPermission: RequestPermission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsBinding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(permissionsBinding.root)

        requestPermission = RequestPermission(this)

        nextPage()

        permissionsBinding.next.setOnClickListener {
            nextPage()
        }

        permissionsBinding.per1.setOnClickListener(this)
        permissionsBinding.per2.setOnClickListener(this)
        permissionsBinding.per3.setOnClickListener(this)
        permissionsBinding.per4.setOnClickListener(this)
        permissionsBinding.per5.setOnClickListener(this)
        permissionsBinding.per6.setOnClickListener(this)
        permissionsBinding.per7.setOnClickListener(this)
    }

    private fun nextPage() {
        if (checkPermissions()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "allow all!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View?) {

        when (view?.id) {

            R.id.per1 -> {
                if (requestPermission.checkCallPhonePermission()) {
                } else {
                    requestPermission.getCallPermission()
                }
            }

            R.id.per2 -> {
                if (requestPermission.checkReadCallLogPermission()) {
                } else {
                    requestPermission.getReadCallLogPermission()
                }
            }

            R.id.per3 -> {
                if (requestPermission.checkReadContactPermission()) {
                } else {
                    requestPermission.getReadContactPermission()
                }
            }

            R.id.per4 -> {
                if (requestPermission.checkListenCallsPermission()) {
                } else {
                    requestPermission.getListenCallsPermission()
                }
            }

            R.id.per5 -> {
                if (requestPermission.checkSendSmsPermission()) {
                } else {
                    requestPermission.getSendSmsPermission()
                }
            }

            R.id.per6 -> {
                if (requestPermission.checkWriteCallLogPermission()) {
                } else {
                    requestPermission.getWriteCallLogPermission()
                }
            }

            R.id.per7 -> {
                if (requestPermission.checkWriteContactPermission()) {
                } else {
                    requestPermission.getWriteContactPermission()
                }
            }
        }

        nextPage()

    }

    private fun checkPermissions(): Boolean {
        var status = true
        permissionsBinding.per1.isChecked = false
        permissionsBinding.per2.isChecked = false
        permissionsBinding.per3.isChecked = false
        permissionsBinding.per4.isChecked = false
        permissionsBinding.per5.isChecked = false
        permissionsBinding.per6.isChecked = false
        permissionsBinding.per7.isChecked = false
        if (requestPermission.checkCallPhonePermission()) {
            permissionsBinding.per1.isChecked = true
        } else {
            status = false
        }
        if (requestPermission.checkReadCallLogPermission()) {
            permissionsBinding.per2.isChecked = true
        } else {
            status = false
        }

        if (requestPermission.checkReadContactPermission()) {
            permissionsBinding.per3.isChecked = true
        } else {
            status = false
        }

        if (requestPermission.checkListenCallsPermission()) {
            permissionsBinding.per4.isChecked = true
        } else {
            status = false
        }

        if (requestPermission.checkSendSmsPermission()) {
            permissionsBinding.per5.isChecked = true
        } else {
            status = false
        }

        if (requestPermission.checkWriteCallLogPermission()) {
            permissionsBinding.per6.isChecked = true
        } else {
            status = false
        }

        if (requestPermission.checkWriteContactPermission()) {
            permissionsBinding.per7.isChecked = true
        } else {
            status = false
        }

        return status
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestPermission.REQUEST_CALLPHONE) {
            if (requestPermission.checkCallPhonePermission()) {
                permissionsBinding.per1.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        } else if (requestCode == requestPermission.REQUEST_READCALLLOG) {
            if (requestPermission.checkReadCallLogPermission()) {
                permissionsBinding.per2.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        } else if (requestCode == requestPermission.REQUEST_READCONTACTS) {
            if (requestPermission.checkReadContactPermission()) {
                permissionsBinding.per3.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        } else if (requestCode == requestPermission.REQUEST_LISTEN_CALLS) {
            if (requestPermission.checkListenCallsPermission()) {
                permissionsBinding.per4.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        } else if (requestCode == requestPermission.REQUEST_SEND_SMS) {
            if (requestPermission.checkSendSmsPermission()) {
                permissionsBinding.per5.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        } else if (requestCode == requestPermission.REQUEST_WRITECALLLOG) {
            if (requestPermission.checkWriteCallLogPermission()) {
                permissionsBinding.per6.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        } else if (requestCode == requestPermission.WRITE_CONTACTS) {
            if (requestPermission.checkWriteContactPermission()) {
                permissionsBinding.per7.isChecked = true
            } else {
                requestPermission.openPermissionSettings(this)
            }
        }
    }
}