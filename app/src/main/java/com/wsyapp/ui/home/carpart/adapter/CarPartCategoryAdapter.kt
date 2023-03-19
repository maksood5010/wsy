package com.wsyapp.ui.home.carpart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R
import com.wsyapp.data.model.response.CarPartCategoryModel

class CarPartCategoryAdapter(
    val context: Context,
    val list: MutableList<CarPartCategoryModel>?
) :
    RecyclerView.Adapter<CarPartCategoryAdapter.ViewHolder>(), View.OnClickListener {

    private var appClickListener: AppClickListener? = null

    fun setAppClickListener(appClickListener: AppClickListener) {
        this.appClickListener = appClickListener
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tv_name: TextView = itemview.findViewById(R.id.tv_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.item_car_part_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name.setOnClickListener(this)
        holder.tv_name.setTag(position)
        val model = list?.get(position) ?: return
        holder.tv_name.setText(model.getName(context))
        if (model.isSelected) {
            holder.tv_name.setBackgroundColor(context.resources.getColor(R.color.colorTheme))
            /*if (Build.VERSION.SDK_INT < 23) {
                holder.tv_name.setTextAppearance(context, R.style.textbtn);
            } else {
                holder.tv_name.setTextAppearance(R.style.textbtn);
            }*/
        } else {
            holder.tv_name.setBackgroundColor(context.resources.getColor(R.color.grayDark))

            /* if (Build.VERSION.SDK_INT < 23) {
                 holder.tv_name.setTextAppearance(context, R.style.textbtn_gray);
             } else {
                 holder.tv_name.setTextAppearance(R.style.textbtn_gray);
             }*/
        }


    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int, model: CarPartCategoryModel)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_price -> {
                if (appClickListener == null) return
                val position = p0!!.getTag() as Int
                appClickListener?.onClickListener(p0, position, list!!.get(position))
                updateUi(position)

            }
        }
    }

    private fun updateUi(position: Int) {
        if (list == null) return

        for (item in list.indices) {
            var get = list.get(item)
            get.isSelected = item == position
            list.set(item, get)
        }

        notifyDataSetChanged()
    }
}