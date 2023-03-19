package com.wsyapp.ui.home.carpart.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.data.model.response.CarPartProductModel

class CarPartItemAdapter(
    val context: Context,
    val list: List<CarPartProductModel>?
) :
    RecyclerView.Adapter<CarPartItemAdapter.ViewHolder>(), View.OnClickListener, Filterable {
    private var filteredList: MutableList<CarPartProductModel>? = null
    private var appClickListener: AppClickListener? = null
    private var subList: MutableList<CarPartProductModel>?=null
    private var searchQuery: String?=""

    init {
        filteredList = list as MutableList<CarPartProductModel>?
        subList=filteredList
    }

    fun setAppClickListener(appClickListener: AppClickListener) {
        this.appClickListener = appClickListener
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tv_name: TextView = itemview.findViewById(R.id.tv_price)
        val tv_desc: TextView = itemview.findViewById(R.id.tv_na)
        val tv_price: TextView = itemview.findViewById(R.id.tv_shop_name)
        val iv_img: ImageView = itemview.findViewById(R.id.iv_img)
        val cl_view: ConstraintLayout = itemview.findViewById(R.id.cl_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.item_car_part_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (filteredList == null || filteredList?.size == 0) {
            appClickListener?.onNoData()
        } else {
            appClickListener?.onDataFound()
        }
        return filteredList?.size ?: 0
    }
    fun searchByName(
        name: String?
    ) {
        var newList = mutableListOf<CarPartProductModel>()
        searchQuery=name
        if (name == null || name.isEmpty()) {
            filteredList = subList
        } else {
            for (item in subList!!) {
                if (item.getName(context)!!.toLowerCase().contains(name.toLowerCase())) {
                    newList.add(item)
                }
            }
            filteredList = newList
        }
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cl_view.setOnClickListener(this)
        holder.cl_view.setTag(position)
        val model = filteredList?.get(position) ?: return
        holder.tv_name.setText(model.getName(context))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_desc.setText(Html.fromHtml(model.details, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_desc.setText(Html.fromHtml(model.details));
        }
        //  holder.tv_desc.setText(Html.fromHtml(model.details))
        holder.tv_price.setText(model.getPriceText(context))

        Glide.with(context).load(model.img).placeholder(R.drawable.bg_image_placeholder)
            .into(holder.iv_img)

    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int, model: CarPartProductModel)
        fun onNoData()
        fun onDataFound()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cl_view -> {
                if (appClickListener == null) return
                val postion = p0!!.getTag() as Int
                if (filteredList == null) return
                appClickListener?.onClickListener(p0, postion, filteredList!!.get(postion))

            }
        }
    }

    inner class MyFilter : Filter() {
        override fun performFiltering(charSeq: CharSequence?): FilterResults {
            val data = charSeq.toString()
            if (data == null || data.isEmpty()) {
                filteredList = list as MutableList<CarPartProductModel>?
            } else if (data == "0") {
                Log.d("TAG", "performFiltering: else if filteredList size ${filteredList!!.size} list Size ${list!!.size}")
                filteredList = list as MutableList<CarPartProductModel>?
            } else {
                val tempList = mutableListOf<CarPartProductModel>()
                for (row in list!!) {
                    if (row.cat == data) {
                        tempList.add(row)
                    }
                }
                Log.d("TAG", "performFiltering: filteredList size ${filteredList!!.size} list Size ${list!!.size}")
                filteredList = tempList
            }
            subList=filteredList
            searchByName(searchQuery)
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
            if (filterResults == null) return
            filteredList = filterResults.values as MutableList<CarPartProductModel>
            subList=filteredList
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return MyFilter()
    }
}