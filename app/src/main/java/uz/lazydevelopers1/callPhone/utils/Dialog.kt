package uz.lazydevelopers1.callPhone.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface


class Dialog(var dialogClick: DialogClick) {

    fun show(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)

        // Set the message show for the Alert time

        // Set the message show for the Alert time
        builder.setMessage(message)

        // Set Alert Title

        // Set Alert Title
        builder.setTitle(title)

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                // When the user click yes button then app will close
                dialogClick.positive()
            } as DialogInterface.OnClickListener)

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                // If user click no then dialog box is canceled.
                dialogClick.negative()
                dialog.cancel()
            } as DialogInterface.OnClickListener)

        // Create the Alert dialog

        // Create the Alert dialog
        val alertDialog: AlertDialog = builder.create()
        // Show the Alert Dialog box
        // Show the Alert Dialog box
        alertDialog.show()
    }

    interface DialogClick {
        fun positive()
        fun negative()
    }

}