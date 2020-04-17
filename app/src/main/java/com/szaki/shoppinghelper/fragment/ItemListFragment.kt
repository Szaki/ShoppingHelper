package com.szaki.shoppinghelper.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.adapter.ItemAdapter
import com.szaki.shoppinghelper.dialog.AddItemDialog
import com.szaki.shoppinghelper.dialog.EditItemDialog
import com.szaki.shoppinghelper.other.GestureController

class ItemListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var itemAdapter: ItemAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_itemlist, container, false)

        itemAdapter = ItemAdapter(requireContext())
        linearLayoutManager = LinearLayoutManager(requireContext())

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            AddItemDialog().show(requireFragmentManager(), "AID")
        }
        fab.setOnLongClickListener {
            itemAdapter.addDefaults()
            true
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.item_view).apply {
            adapter = itemAdapter
            layoutManager = linearLayoutManager
        }
        val itemTouchHelper = ItemTouchHelper(GestureController(itemAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    override fun onResume() {
        itemAdapter.downloadLists()
        super.onResume()
    }

    fun onEIDPositiveClick(dialog: DialogFragment) {
        val pos = itemAdapter.items.indexOf((dialog as EditItemDialog).item)
        val item = dialog.readItem()
        itemAdapter.edit(pos, item)
    }

    fun onAIDPositiveClick(dialog: DialogFragment) {
        val item = (dialog as AddItemDialog).readItem()
        itemAdapter.add(item)
    }
}