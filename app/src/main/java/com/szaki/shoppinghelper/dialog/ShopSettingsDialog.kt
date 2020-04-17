package com.szaki.shoppinghelper.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.data.Shop
import com.szaki.shoppinghelper.other.FileHandler
import kotlinx.android.synthetic.main.shopsetting.view.*

class ShopSettingsDialog() : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var shopSettingAdapter: ShopSettingAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.dialog_shop, null)

        shopSettingAdapter = ShopSettingAdapter(this)
        linearLayoutManager = LinearLayoutManager(this)

        recyclerView = view.findViewById<RecyclerView>(R.id.shopsetting_view).apply {
            adapter = shopSettingAdapter
            layoutManager = linearLayoutManager
        }

        view.findViewById<Button>(R.id.closeshops).setOnClickListener {
            finish()
        }

        setContentView(view)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onStop() {
        super.onStop()
        shopSettingAdapter.writeToDisk()
    }

    inner class ShopSettingAdapter : RecyclerView.Adapter<ShopSettingAdapter.ViewHolder> {

        var shops: ArrayList<Shop>
        var fileHandler: FileHandler

        constructor(context: Context) : super() {
            fileHandler = FileHandler(context)
            shops = fileHandler.readArrayList("shops.ser")
            if (shops.size == 0 || shops.last().name.trim().isNotEmpty())
                add(Shop(""))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.shopsetting, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = shops.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.shopsetting_name.setText(shops[position].name)

            if (holder.shopsetting_name.text.trim().isNotEmpty()) {
                holder.delete_shop.visibility = View.VISIBLE
            }

            holder.shopsetting_name.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.trim().isEmpty())
                        holder.delete_shop.visibility = View.GONE
                }
            })

            holder.save_shop.setOnClickListener {
                if (holder.shopsetting_name.text.trim().isNotEmpty()) {
                    shops[position].name = holder.shopsetting_name.text.toString()
                } else {
                    remove(shops[position])
                }
                holder.save_shop.visibility = View.GONE
                if (shops.size == 0 || shops.last().name.trim().isNotEmpty()) {
                    add(Shop(""))
                    recyclerView.smoothScrollToPosition(shops.size - 1)
                }
                notifyDataSetChanged()
            }

            holder.shopsetting_name.setOnFocusChangeListener { v, hasFocus ->
                if (holder.shopsetting_name.isFocused)
                    holder.save_shop.visibility = View.VISIBLE
                else {
                    holder.save_shop.visibility = View.GONE
                }
                true
            }

            holder.delete_shop.setOnClickListener {
                remove(shops[position])
                if (shops.size == 0 || shops.last().name.trim().isNotEmpty())
                    add(Shop(""))
            }
        }

        fun remove(name: String) {
            val existing = shops.find { it.name == name }
            if (existing != null)
                shops.remove(existing)
            notifyDataSetChanged()
        }

        fun remove(shop: Shop) {
            shops.remove(shop)
            notifyDataSetChanged()
        }


        fun add(shop: Shop) {
            if (shops.find { it.name == shop.name } == null)
                shops.add(shop)
            notifyDataSetChanged()
        }

        fun add(shop: String) {
            if (shops.find { it.name == shop } == null)
                shops.add(Shop(shop))
            notifyDataSetChanged()
        }

        fun writeToDisk() {
            for (s in shops) {
                if (s.name.trim().isEmpty())
                    shops.remove(s)
            }
            fileHandler.writeArrayList(shops, "shops.ser")
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val shopsetting_name = itemView.shopsetting_name
            val save_shop = itemView.save_shop
            val delete_shop = itemView.delete_shop
        }
    }
}