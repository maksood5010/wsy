package com.wsyapp.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.wsyapp.R

class SpinnerAdapter(val context: Context, val list: List<String>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_spinner, p2, false)
        var tv_title = view.findViewById<TextView>(R.id.tv_title)
        tv_title.setText(list[p0])
        return view
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return if (list == null) 0 else list.size
    }
}