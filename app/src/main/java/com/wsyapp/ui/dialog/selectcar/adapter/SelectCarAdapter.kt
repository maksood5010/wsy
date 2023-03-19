package com.wsyapp.ui.dialog.selectcar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R

class SelectCarAdapter(var context: Context, val appClickListener: AppClickListener) :
    RecyclerView.Adapter<SelectCarAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cl_view: ConstraintLayout = itemView.findViewById(R.id.cl_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_select_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cl_view.setTag(position)
        holder.cl_view.setOnClickListener(this)
    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cl_view -> {
                if (appClickListener == null) return
                appClickListener.onClickListener(p0, p0!!.getTag() as Int)
            }
        }
    }
}