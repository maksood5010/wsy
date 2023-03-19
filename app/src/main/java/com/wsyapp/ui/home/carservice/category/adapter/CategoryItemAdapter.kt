package com.wsyapp.ui.home.carservice.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R
import com.wsyapp.ui.home.carservice.CarServicesFragment

class CategoryItemAdapter(val context: Context, val appClickListener: AppClickListener) :
    RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tv_go: TextView = itemview.findViewById(R.id.tv_go)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_go.setOnClickListener(this)
        holder.tv_go.setTag(position)

    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
    }
    public interface SpinnerClickListener {
        fun onSpinnerListener(service: String)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_go -> {
                if (appClickListener == null) return
                appClickListener.onClickListener(p0, p0!!.getTag() as Int)

            }
        }
    }

}