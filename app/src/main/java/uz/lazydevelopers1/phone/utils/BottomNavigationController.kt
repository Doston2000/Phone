package uz.lazydevelopers1.phone.utils

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import uz.lazydevelopers1.phone.R
import uz.lazydevelopers1.phone.utils.SelectFragment

class BottomNavigationController(
    var context: Context,
    var keypadText: TextView,
    var keypadView: View,
    var recentText: TextView,
    var recentView: View,
    var contactText: TextView,
    var contactView: View,
    var keypad: LinearLayout,
    var recent: LinearLayout,
    var contact: LinearLayout,
) {

    init {
        keypad.isEnabled = false
    }

    fun selectBtn(selectFragment: SelectFragment) {
        unSelectBtn()
        when (selectFragment) {
            SelectFragment.KEYPAD -> {
                keypad.isEnabled = false
                keypadText.setTextColor(context.resources.getColor(R.color.select_bottom_navigation_text_color))
                keypadView.setBackgroundResource(R.drawable.select_line_bg)
            }

            SelectFragment.LOG -> {
                recent.isEnabled = false
                recentText.setTextColor(context.resources.getColor(R.color.select_bottom_navigation_text_color))
                recentView.setBackgroundResource(R.drawable.select_line_bg)
            }

            SelectFragment.CONTACT -> {
                contact.isEnabled = false
                contactText.setTextColor(context.resources.getColor(R.color.select_bottom_navigation_text_color))
                contactView.setBackgroundResource(R.drawable.select_line_bg)
            }
        }
    }

    private fun unSelectBtn() {
        keypad.isEnabled = true
        recent.isEnabled = true
        contact.isEnabled = true
        keypadText.setTextColor(context.resources.getColor(R.color.default_bottom_navigation_text_color))
        keypadView.setBackgroundResource(R.drawable.default_line_bg)
        recentText.setTextColor(context.resources.getColor(R.color.default_bottom_navigation_text_color))
        recentView.setBackgroundResource(R.drawable.default_line_bg)
        contactText.setTextColor(context.resources.getColor(R.color.default_bottom_navigation_text_color))
        contactView.setBackgroundResource(R.drawable.default_line_bg)
    }

}