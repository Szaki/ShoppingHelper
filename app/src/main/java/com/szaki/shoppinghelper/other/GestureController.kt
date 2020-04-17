package com.szaki.shoppinghelper.other

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.adapter.ItemAdapter

class GestureController(val adapter: ItemAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        adapter.saveLists()
        if (p1 == ItemTouchHelper.LEFT) {
            adapter.remove(p0.adapterPosition)
            adapter.undo(adapter.context.getString(R.string.itemremoved))
        }
        if (p1 == ItemTouchHelper.RIGHT) {
            adapter.postpone(p0.adapterPosition)
            adapter.undo(adapter.context.getString(R.string.itempostponed))
        }
    }

}