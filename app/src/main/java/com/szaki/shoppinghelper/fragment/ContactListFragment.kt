package com.szaki.shoppinghelper.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.data.Item
import com.szaki.shoppinghelper.other.FileHandler

class ContactListFragment : Fragment() {

    lateinit var items: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        items = FileHandler(requireContext()).readArrayList("futureitems.ser")
        if (items.size == 0)
            items = FileHandler(requireContext()).readArrayList("items.ser")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contactlist, container, false)

        view.findViewById<Button>(R.id.notify_start).setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_start))
            intent.type = "text/plain"
            intent.setPackage("com.facebook.orca")
            try {
                startActivity(intent)
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(context, getString(R.string.error_messenger), Toast.LENGTH_LONG).show()
            }
        }

        view.findViewById<Button>(R.id.notify_end).setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_end, getPostponedItems()))
            intent.type = "text/plain"
            intent.setPackage("com.facebook.orca")
            try {
                startActivity(intent)
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(context, getString(R.string.error_messenger), Toast.LENGTH_LONG).show()
            }
        }
        return view
    }

    fun getPostponedItems(): String {
        var output = ""
        for (i in items) {
            output += i.name
            if (i != items.last())
                output += ", "
        }
        return output + "."
    }
}