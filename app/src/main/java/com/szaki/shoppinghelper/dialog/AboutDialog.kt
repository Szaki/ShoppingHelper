package com.szaki.shoppinghelper.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.szaki.shoppinghelper.R

class AboutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(getContentView())

        return builder.create()
    }

    private fun getContentView(): View = LayoutInflater.from(context).inflate(R.layout.dialog_about, null)
}