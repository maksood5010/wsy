package com.wsyapp.ui.right_menu.term.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R
import com.wsyapp.data.model.response.FaqModel

class PolicyAdapter(
    val context: Context,
    val dataList: List<FaqModel>
) :
    RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tv_title: TextView = itemview.findViewById(R.id.tv_title)
        val tv_details: TextView = itemview.findViewById(R.id.tv_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.privacy_entry, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]

        val title = model.title
        if (title == null || title.isEmpty()) return
        try {
            val value = title.toInt()
            if (value == 1) {
                holder.tv_title.setText(model.getParagraphText())
                holder.tv_title.visibility = View.VISIBLE
                holder.tv_details.visibility = View.GONE
            } else {
                holder.tv_details.setText(model.getParagraphText())
                holder.tv_details.visibility = View.VISIBLE
                holder.tv_title.visibility = View.GONE

            }
        } catch (e: NumberFormatException) {

        }


    }
}