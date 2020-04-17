package com.szaki.shoppinghelper.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.data.Item
import com.szaki.shoppinghelper.data.SimpleItem
import com.szaki.shoppinghelper.other.FileHandler
import kotlinx.android.synthetic.main.dialog_item.view.*


class EditItemDialog() : DialogFragment() {

    internal lateinit var listener: DialogListener
    lateinit var item: Item
    lateinit var namelist: Array<String>
    lateinit var catlist: Array<String>
    lateinit var fileHandler: FileHandler
    private lateinit var edititemname: AutoCompleteTextView
    private lateinit var edititemcat: AutoCompleteTextView
    private lateinit var edititemnum: EditText

    fun createDialog(item: Item): EditItemDialog {
        val dialog = EditItemDialog()
        val bundle = Bundle()
        bundle.putParcelable("item", item)
        dialog.arguments = bundle
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileHandler = FileHandler(requireContext())
        val list = mutableListOf<String>()
        for (thing in fileHandler.readArrayList<SimpleItem>("itemhistory.ser"))
            list.add(thing.name)
        namelist = list.toTypedArray()

        list.clear()

        for (thing in fileHandler.readArrayList<SimpleItem>("cathistory.ser"))
            list.add(thing.name)
        catlist = list.toTypedArray()
        item = arguments?.getParcelable("item") ?: Item("", "", 0.0)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.edititemdialog_title))
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onEIDPositiveClick(this)
        }
        builder.setView(getContentView())

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as DialogListener
    }

    fun readItem(): Item {
        if (edititemnum.text.toString().isEmpty())
            return Item(edititemname.text.toString(), edititemcat.text.toString(), 1.0)
        else return Item(edititemname.text.toString(), edititemcat.text.toString(), edititemnum.text.toString().toDouble())
    }

    private fun getContentView(): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_item, null)

        edititemname = view.edit_item_name
        edititemcat = view.edit_item_cat
        edititemnum = view.edit_item_num

        edititemname.setText(item.name)
        edititemcat.setText(item.cat)
        edititemnum.setText(item.num.toString())

        edititemname.setAdapter(ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, namelist))
        edititemcat.setAdapter(ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, catlist))

        return view
    }
}