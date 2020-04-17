package com.szaki.shoppinghelper.dialog

import android.support.v4.app.DialogFragment

interface DialogListener {
    fun onEIDPositiveClick(dialog: DialogFragment)
    fun onAIDPositiveClick(dialog: DialogFragment)
}