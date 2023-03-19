package com.wsyapp.ui.home.cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsyapp.R

class PaymentListAdapter(context: Context) :
    RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {
    private var context: Context = context
    private var checked: Int = -1
    private var check: Int = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radio: RadioButton = itemView.findViewById(R.id.radio)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.radio_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> holder.radio.setText(context.getString(R.string.cashdelivery))
            1 -> holder.radio.setText(context.getString(R.string.wallet))
            2 -> holder.radio.setText(context.getString(R.string.card))
        }
        if ((checked == -1 && position == 0))
            holder.radio.isChecked = true
        else
            if (checked == position)
                holder.radio.isChecked = true
            else
                holder.radio.isChecked = false

        holder.radio.setOnClickListener {
            checked = position
            check = position
            notifyDataSetChanged()
        }

    }
    fun getPaymentMethod(): Int{
        return  check+1
    }
}