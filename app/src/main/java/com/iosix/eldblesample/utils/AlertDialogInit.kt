package com.iosix.eldblesample.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.iosix.eldblesample.R

class AlertDialogInit {

    companion object {

        fun showDialogOk(
            context: Context,
            title: Int,
            listener: DialogInterface.OnClickListener
        ) {
            val dialog: AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(R.string.yes, listener)
                .setNegativeButton(R.string.no, listener)
            dialog = builder.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                Utils.getColors(
                    R.color.colorPrimary
                )
            )
        }

        fun showDialogOkWithMessage(
            context: Context,
            title: Int,
            message: Int,
            listener: DialogInterface.OnClickListener
        ) {
            val dialog: AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, listener)
                .setNegativeButton(R.string.no, listener)
            dialog = builder.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                Utils.getColors(
                    R.color.colorPrimary
                )
            )
        }

        fun showDialogCustom(
            context: Context,
            title: Int,
            message: Int,
            positiveButtonTitled: Int,
            negativeButtonTitled: Int,
            listener: DialogInterface.OnClickListener
        ) {
            val dialog: AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonTitled, listener)
                .setNegativeButton(negativeButtonTitled, listener)
            dialog = builder.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                Utils.getColors(
                    R.color.colorPrimary
                )
            )
        }

    }
}
