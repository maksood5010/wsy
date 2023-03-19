package com.wsyapp.ui.home.garage.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.data.model.response.GarageCategoryModel

class GarageCategoryAdapter(val context: Context, val list: List<GarageCategoryModel?>) :
    RecyclerView.Adapter<GarageCategoryAdapter.ViewHolder>(), View.OnClickListener {

    private var appClickListener: AppClickListener? = null

    fun setAppClickListener(appClickListener:AppClickListener){
        this.appClickListener=appClickListener
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val iv_img: ImageView = itemview.findViewById(R.id.iv_img)
        val tv_name: TextView = itemview.findViewById(R.id.tv_price)
        val cl_view: ConstraintLayout = itemview.findViewById(R.id.cl_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_main_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cl_view.setOnClickListener(this)
        holder.cl_view.setTag(position)

        val model = list[position] ?: return
        Glide.with(context).load(model.img).placeholder(R.drawable.bg_image_placeholder)
            .into(holder.iv_img)
        holder.tv_name.setText(model.getName(context))

    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cl_view -> {
                if (appClickListener == null) return
                appClickListener!!.onClickListener(p0, p0!!.getTag() as Int)

            }
        }
    }
}