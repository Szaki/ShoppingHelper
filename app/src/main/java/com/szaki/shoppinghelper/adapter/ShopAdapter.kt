package com.szaki.shoppinghelper.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.data.Shop
import com.szaki.shoppinghelper.other.FileHandler
import kotlinx.android.synthetic.main.shop.view.*


class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    var shops: ArrayList<Shop>
    var fileHandler: FileHandler

    constructor(context: Context) : super() {
        fileHandler = FileHandler(context)
        shops = fileHandler.readArrayList("shops.ser")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = shops.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.shopname.text = shops[position].name
        holder.shopselected.isChecked = shops[position].selected

        holder.shopselected.setOnCheckedChangeListener { buttonView, isChecked ->
            shops[position].selected = isChecked
        }
    }

    fun readFromDisk() {
        shops = fileHandler.readArrayList("shops.ser")
        notifyDataSetChanged()
    }

    fun getSelectedStores(): String {
        var output = ""
        val suffix = "+OR+"
        for (shop in shops)
            if (shop.selected)
                output += (shop.name + suffix)
        if (output.endsWith(suffix))
            output = output.removeSuffix(suffix)
        return output
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shopname = itemView.shop_name
        val shopselected = itemView.shop_selected
    }
}