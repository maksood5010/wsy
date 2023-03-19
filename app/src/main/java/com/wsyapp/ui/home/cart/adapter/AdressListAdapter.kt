package com.wsyapp.ui.home.cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wsyapp.R

class AdressListAdapter(context: Context, bottomSheetDialog: BottomSheetDialog) :
    RecyclerView.Adapter<AdressListAdapter.ViewHolder>() {
    private var context: Context = context
    private var bottomDialog: BottomSheetDialog = bottomSheetDialog

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSelect:TextView=itemView.findViewById(R.id.tv_select_add)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_select_address, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSelect.setOnClickListener {
         bottomDialog.dismiss()
        }
    }
}