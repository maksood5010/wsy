package com.wsyapp.ui.home.garage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.data.model.response.GarageSubCategoryModel

class GarageItemAdapter(
    var context: Context,
    var list: MutableList<GarageSubCategoryModel>?,
    val appClickListener: AppClickListener?
) :
    RecyclerView.Adapter<GarageItemAdapter.ViewHolder>(), View.OnClickListener, Filterable {

    var listFilter: List<GarageSubCategoryModel>? = null
    private var selectedList: MutableList<GarageSubCategoryModel>? = null

    init {
        listFilter = list
    }

    fun selectAllSelection() {
        if (list == null) return
        for (item in list!!) {
            item.isSelected = true
        }
        selectedList = list
        appClickListener?.onAllItemCheckedListener(selectedList)
        notifyDataSetChanged()
    }

    fun removeAllSelection() {
        if (list == null) return
        for (item in list!!) {
            item.isSelected = false
        }
        selectedList = list
        appClickListener?.onAllItemRemovedListener(selectedList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cl_view: ConstraintLayout = itemView.findViewById(R.id.cl_view)
        val iv_garage: ImageView = itemView.findViewById(R.id.iv_garage)
        val tv_name: TextView = itemView.findViewById(R.id.tv_price)
        val tv_address: TextView = itemView.findViewById(R.id.tv_address)
        val chk_box: CheckBox = itemView.findViewById(R.id.chk_box)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_garage, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (listFilter != null) listFilter!!.size
        else 0
    }

    interface AppClickListener {
        fun onClickListener(view: View, position: Int, model: GarageSubCategoryModel?)
        fun onItemCheckedListener(view: View, position: Int, model: GarageSubCategoryModel?)
        fun onItemRemovedListener(view: View, position: Int, model: GarageSubCategoryModel?)
        fun onAllItemCheckedListener(selectedList: MutableList<GarageSubCategoryModel>?)
        fun onAllItemRemovedListener(selectedList: MutableList<GarageSubCategoryModel>?)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cl_view -> {
                if (appClickListener == null) return
                if (listFilter != null) {
                    val model = listFilter!!.get(p0.getTag() as Int)
                    appClickListener.onClickListener(p0, p0.getTag() as Int, model)
                    return
                }
                appClickListener.onClickListener(p0, p0.getTag() as Int, null)
            }
        }
    }

    override fun onBindViewHolder(holder: GarageItemAdapter.ViewHolder, position: Int) {


        val model = listFilter?.get(position) ?: return
        Glide.with(context).load(model.cover).placeholder(R.drawable.bg_image_placeholder)
            .into(holder.iv_garage)

        holder.chk_box.setOnCheckedChangeListener(null)
        holder.chk_box.isChecked = model.isSelected

        holder.tv_name.setText(model.getName(context))
        holder.tv_address.setText(model.address)

        holder.cl_view.setTag(position)
        holder.chk_box.setTag(position)

        holder.cl_view.setOnClickListener(this)

        holder.chk_box.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                model.isSelected = p1
                if (p1)
                    appClickListener?.onItemCheckedListener(holder.chk_box, position, model)
                        ?: return
                else
                    appClickListener?.onItemRemovedListener(holder.chk_box, position, model)
                        ?: return
            }

        })

    }

    inner class LocationFilter : Filter() {
        override fun performFiltering(charSeq: CharSequence?): FilterResults {
            val data = charSeq.toString()
            if (data == null || data.isEmpty()) {
                listFilter = list
            } else if (data == "0") {
                listFilter = list
            } else {
                var filteredList = mutableListOf<GarageSubCategoryModel>()
                for (row in list!!) {
                    if (row.city_id == data) {
                        filteredList.add(row)
                    }
                }
                listFilter = filteredList
            }
            val filterResults = FilterResults()
            filterResults.values = listFilter
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
            if (filterResults != null) {
                listFilter = filterResults.values as MutableList<GarageSubCategoryModel>
                notifyDataSetChanged()

            }
        }

    }

    override fun getFilter(): Filter {
        return LocationFilter()
    }
}