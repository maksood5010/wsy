package com.wsyapp.ui.home.cart.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.data.database.entity.Cart

class ProductListAdapter(context: Context, cartList: MutableList<Cart>?) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    private var context: Context = context
    private var cartLis: MutableList<Cart>? = cartList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val tv_price: TextView = itemView.findViewById(R.id.tv_price)
        val iv_product: ImageView = itemView.findViewById(R.id.iv_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartLis!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list= cartLis!![position]
        Glide.with(context).load(list.img).placeholder(R.drawable.bg_image_placeholder).into(holder.iv_product)
        holder.tv_name.setText(list.getName(context))
        holder.tv_quantity.setText((list.quantity).toString())
        holder.tv_price.setText(list.getPriceTotal(context,list))

    }
}