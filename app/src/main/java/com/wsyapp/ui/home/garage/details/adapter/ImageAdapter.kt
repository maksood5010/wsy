package com.wsyapp.ui.home.garage.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R

class ImageAdapter(var context: Context, val appClickListener: AppClickListener) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_view: ImageView = itemView.findViewById(R.id.iv_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.iv_view.setTag(position)
        holder.iv_view.setOnClickListener(this)
    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_view -> {
                if (appClickListener == null) return
                appClickListener.onClickListener(p0, p0!!.getTag() as Int)
            }
        }
    }
}