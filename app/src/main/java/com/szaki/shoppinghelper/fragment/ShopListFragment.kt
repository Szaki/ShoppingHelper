package com.szaki.shoppinghelper.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.adapter.ShopAdapter


class ShopListFragment : Fragment() {

    lateinit var shopAdapter: ShopAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shoplist, container, false)

        shopAdapter = ShopAdapter(requireContext())
        linearLayoutManager = LinearLayoutManager(requireContext())

        view.findViewById<RecyclerView>(R.id.shop_view).apply {
            adapter = shopAdapter
            layoutManager = linearLayoutManager
        }

        view.findViewById<Button>(R.id.search).setOnClickListener {
            val uri = Uri.parse("geo:0,0?q=supermarkets")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(context, getString(R.string.error_maps), Toast.LENGTH_LONG).show()
            }
        }

        view.findViewById<Button>(R.id.search_spec).setOnClickListener {
            val stores = shopAdapter.getSelectedStores()
            if (stores.isNotEmpty()) {
                val uri = Uri.parse("geo:0,0?q=$stores")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                try {
                    startActivity(intent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, getString(R.string.error_maps), Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        shopAdapter.readFromDisk()
    }
}