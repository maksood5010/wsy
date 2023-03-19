package com.wsyapp.ui.leftmenu.profile.cars.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.wsyapp.R
import com.wsyapp.data.model.response.CarsTypeModel

class CarTypeAdapter(val context: Context, val carTypeList: MutableList<CarsTypeModel>?) :
    BaseAdapter(), Filterable {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_spinner, p2, false)
        var tv_title = view.findViewById<TextView>(R.id.tv_title)
        tv_title.setText(carTypeList!!.get(p0).getTypeName(context))
        return view
    }

    override fun getItem(p0: Int): Any {
        return carTypeList!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return carTypeList?.size ?: 0
    }

    override fun getFilter(): Filter {
        return MyFilter()
    }

    class MyFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {

            return FilterResults()
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

        }

    }
}