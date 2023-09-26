package uz.lazydevelopers1.phone.ui.keypad

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import uz.lazydevelopers1.phone.FragmentListener
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.databinding.FragmentKeypadBinding
import uz.lazydevelopers1.phone.ui.recents.adapters.LogAdapter
import uz.lazydevelopers1.phone.utils.CommunicationOptions
import uz.lazydevelopers1.phone.utils.RequestPermission

class KeypadFragment : Fragment(), OnClickListener {

    private lateinit var keypadBinding: FragmentKeypadBinding
    private var phoneNumber = ""


    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (phoneNumber.isEmpty()) {
                fragmentListener?.backPress()
            } else {
                keypadBinding.input.setText("")
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
        keypadBinding = FragmentKeypadBinding.inflate(inflater, container, false)

        keypadBinding.apply {

            one.setOnClickListener(this@KeypadFragment)
            two.setOnClickListener(this@KeypadFragment)
            three.setOnClickListener(this@KeypadFragment)
            four.setOnClickListener(this@KeypadFragment)
            five.setOnClickListener(this@KeypadFragment)
            six.setOnClickListener(this@KeypadFragment)
            seven.setOnClickListener(this@KeypadFragment)
            eight.setOnClickListener(this@KeypadFragment)
            nine.setOnClickListener(this@KeypadFragment)
            x.setOnClickListener(this@KeypadFragment)
            zero.setOnClickListener(this@KeypadFragment)
            c.setOnClickListener(this@KeypadFragment)

            zero.setOnLongClickListener {
                writeNumber("+")
                true
            }

            backspace.setOnClickListener {
                input.setText(
                    phoneNumber.substring(
                        0,
                        (if (phoneNumber.isEmpty()) 0 else phoneNumber.length - 1)
                    )
                )
            }

            backspace.setOnLongClickListener {
                input.setText("")
                true
            }

            callBtn.setOnClickListener {
                call()
            }

            input.addTextChangedListener {
                if (input.text.toString() == "") {
                    backspace.visibility = View.INVISIBLE
                } else {
                    backspace.visibility = View.VISIBLE
                }
                phoneNumber = input.text.toString()
                fragmentListener?.listenKeypadInput(phoneNumber)
            }

        }

        return keypadBinding.root
    }

    private fun call() {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.one -> {
                writeNumber("1")
            }

            R.id.two -> {
                writeNumber("2")
            }

            R.id.three -> {
                writeNumber("3")
            }

            R.id.four -> {
                writeNumber("4")
            }

            R.id.five -> {
                writeNumber("5")
            }

            R.id.six -> {
                writeNumber("6")
            }

            R.id.seven -> {
                writeNumber("7")
            }

            R.id.eight -> {
                writeNumber("8")
            }

            R.id.nine -> {
                writeNumber("9")
            }

            R.id.x -> {
                writeNumber("*")
            }

            R.id.zero -> {
                writeNumber("0")
            }

            R.id.c -> {
                writeNumber("#")
            }
        }
    }

    private fun writeNumber(number: String) {
        keypadBinding.apply {
            input.setText(input.text.toString() + number)
        }
    }

    // listen keypad fragment from main ativity
    var fragmentListener: FragmentListener? = null

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