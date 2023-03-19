package com.wsyapp.ui.home.garage.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R
import com.wsyapp.data.model.response.GarageServiceModel

class GarageServiceAdapter(val context: Context, val list: List<GarageServiceModel>?) :
    RecyclerView.Adapter<GarageServiceAdapter.ViewHolder>(), View.OnClickListener {
    private var appClickListener: AppClickListener? = null

    fun setAppClickListeners(listener: AppClickListener) {
        appClickListener = listener
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val chk_box: CheckBox = itemview.findViewById(R.id.chk_box)
        val tv_service: TextView = itemview.findViewById(R.id.tv_service)
        val tv_price: TextView = itemview.findViewById(R.id.tv_shop_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_garage_service, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chk_box.setOnClickListener(this)
        holder.chk_box.setTag(position)

        val model = list?.get(position) ?: return
        holder.tv_price.setText("" + model.price)
        holder.tv_service.setText(model.getName(context))

    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.chk_box -> {
                if (appClickListener == null) return
                appClickListener?.onClickListener(p0, p0!!.getTag() as Int)

            }
        }
    }
}