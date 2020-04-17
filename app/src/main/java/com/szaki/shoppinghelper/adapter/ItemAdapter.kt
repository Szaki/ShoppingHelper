package com.szaki.shoppinghelper.adapter

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.szaki.shoppinghelper.MainActivity
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.data.Item
import com.szaki.shoppinghelper.data.SimpleItem
import com.szaki.shoppinghelper.dialog.EditItemDialog
import com.szaki.shoppinghelper.other.FileHandler
import kotlinx.android.synthetic.main.item.view.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    var items = ArrayList<Item>()
    var futureitems = ArrayList<Item>()
    var defaultitems = ArrayList<Item>()
    var item_history: ArrayList<SimpleItem>
    var cat_history: ArrayList<SimpleItem>
    lateinit var dialog: EditItemDialog
    var fileHandler: FileHandler
    var context: Context


    constructor(context: Context) : super() {
        this.context = context
        fileHandler = FileHandler(context)
        item_history = fileHandler.readArrayList("itemhistory.ser")
        cat_history = fileHandler.readArrayList("cathistory.ser")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item_name.text = items[position].name
        holder.item_cat.text = items[position].cat
        if (items[position].num.toString().endsWith(".0"))
            holder.item_num.text = items[position].num.toString().substringBefore(".")
        else holder.item_num.text = items[position].num.toString()

        holder.itemView.setOnLongClickListener {
            dialog = EditItemDialog().createDialog(items[position])
            dialog.show((context as MainActivity).supportFragmentManager, "EID")
            return@setOnLongClickListener true
        }
    }

    fun addDefaults(){
        val defaults = ArrayList<Item>()
        defaults.addAll(defaultitems)
        for (item in defaults){
            val other = items.find { it.name == item.name }
            if (other != null) {
                item.num += other.num
                items.remove(other)
            }
            if (item.name?.trim() != "" && item.num > 0) {
                items.add(item)
            }
        }
        items.sortBy { it.cat }
        uploadLists()
        notifyDataSetChanged()
    }

    fun remove(pos: Int) {
        items.removeAt(pos)
        if (items.size == 0) {
            items.addAll(futureitems)
            futureitems.clear()
        }
        uploadLists()
        notifyDataSetChanged()
    }

    fun postpone(pos: Int) {
        val other = futureitems.find { it.name == items[pos].name }
        if (other != null) {
            items[pos].num += other.num
            futureitems.remove(other)
        }
        futureitems.add(items[pos])
        futureitems.sortBy { it.cat }
        remove(pos)
    }

    fun saveLists() {
        fileHandler.writeArrayList(items, "items_undo.ser")
        fileHandler.writeArrayList(futureitems, "futureitems_undo.ser")
    }

    fun undo(message: String) {
        Snackbar.make((context as MainActivity).findViewById(R.id.coordlayout), message, Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.Undo)) {
                    items = fileHandler.readArrayList("items_undo.ser")
                    futureitems = fileHandler.readArrayList("futureitems_undo.ser")
                    notifyDataSetChanged()
                }.show()
    }

    fun add(item: Item) {
        val other = items.find { it.name == item.name }
        if (other != null) {
            item.num += other.num
            items.remove(other)
        }
        if (item.name?.trim() != "" && item.num > 0) {
            items.add(item)
            items.sortBy { it.cat }

            val olditem = item_history.find { it.name == item.name }
            if (olditem != null)
                item_history.get(item_history.indexOf(olditem)).num += item.num
            else item_history.add(SimpleItem(item.name!!, item.num))
            item_history.sortBy { it.num }

            val oldcategory = cat_history.find { it.name == item.cat }
            if (oldcategory != null)
                cat_history.get(cat_history.indexOf(oldcategory)).num++
            else cat_history.add(SimpleItem(item.cat!!, 1.0))
            cat_history.sortBy { it.num }
        }
        uploadLists()
        fileHandler.writeArrayList(item_history, "itemhistory.ser")
        fileHandler.writeArrayList(cat_history, "cathistory.ser")
        notifyDataSetChanged()
    }

    fun edit(pos: Int, item: Item) {
        items.removeAt(pos)
        val other = items.find { it.name == item.name }
        if (other != null) {
            item.num += other.num
            items.remove(other)
        }
        if (item.name?.trim() != "" && item.num > 0)
            items.add(pos, item)
        items.sortBy { it.cat }
        uploadLists()
        notifyDataSetChanged()
    }

    fun downloadLists() {
        fileHandler.downloadList("items.ser") {
            items = fileHandler.readArrayList("items.ser")
            notifyDataSetChanged()
        }
        fileHandler.downloadList("futureitems.ser") {
            futureitems = fileHandler.readArrayList("futureitems.ser")
        }
        fileHandler.downloadList("defaultitems.ser"){
            defaultitems = fileHandler.readArrayList("defaultitems.ser")
        }
    }

    fun uploadLists() {
        fileHandler.writeArrayList(items, "items.ser")
        fileHandler.writeArrayList(futureitems, "futureitems.ser")
        fileHandler.uploadList("items.ser")
        fileHandler.uploadList("futureitems.ser")
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_name = itemView.item_name
        val item_cat = itemView.item_cat
        val item_num = itemView.item_num
    }
}