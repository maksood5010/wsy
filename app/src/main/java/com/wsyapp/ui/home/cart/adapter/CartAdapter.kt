package com.wsyapp.ui.home.cart.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.data.database.entity.Cart
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToLong

class CartAdapter(
    val context: Context,
    val cartList: MutableList<Cart>?
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>(), View.OnClickListener {

    var appClickListener: AppClickListener? = null
    var stockQuantity = 0

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val cl_view: ConstraintLayout = itemview.findViewById(R.id.cl_view)
        val iv_garage: ImageView = itemview.findViewById(R.id.iv_garage)
        val tv_car_name: TextView = itemview.findViewById(R.id.tv_car_name)
        val tv_price: TextView = itemview.findViewById(R.id.tv_shop_name)
        val tv_quantity: TextView = itemview.findViewById(R.id.tv_quantity)
        val iv_minus: ImageView = itemview.findViewById(R.id.iv_minus)
        val iv_add: ImageView = itemview.findViewById(R.id.iv_add)
        val iv_delete: ImageView = itemview.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cl_view.setTag(position)
        holder.iv_minus.setTag(position)
        holder.iv_add.setTag(position)
        holder.iv_delete.setTag(position)

        holder.cl_view.setOnClickListener(this)
        holder.iv_delete.setOnClickListener(this)
        val model = cartList?.get(position) ?: return

        holder.iv_minus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val trim = holder.tv_quantity.text.toString().trim()
                val total = trim.toInt()
                if (total == 0) return
                val i = total-1
                model.quantity = i
                if (i == 0) {
//                    appClickListener?.onClickListener(holder.iv_delete, p0!!.getTag() as Int)
                } else {
                    appClickListener?.onMinusQuantity(holder.tv_quantity, p0!!.getTag() as Int, model)
                    holder.tv_quantity.setText("" + i)
                }
            }

        })
        holder.iv_add.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val trim = holder.tv_quantity.text.toString().trim()
                val total = trim.toInt()
                val i = total + 1
//                if (i > stockQuantity)
//                    return
//                holder.tv_quantity.setText("" + i)
                model.quantity = i
                appClickListener?.onAddedQuantity(holder.tv_quantity, p0!!.getTag() as Int, model)
            }

        })


        Glide.with(context).load(model.img).placeholder(R.drawable.bg_image_placeholder)
            .into(holder.iv_garage)
        holder.tv_car_name.setText(model.getName(context))
        holder.tv_price.setText(getTotal(model))
        holder.tv_quantity.setText("" + model.quantity)
    }

    private fun getTotal(model: Cart): String {
        var num = model.price.toDouble()
        val total=num * model.quantity
        val number2digits:Double = String.format("%.3f", total).toDouble()
        return (number2digits).toString()+" "+context.getString(R.string.aed)
    }


    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
        fun onAddedQuantity(view: TextView, position: Int, model: Cart)
        fun onMinusQuantity(view: View, position: Int, model: Cart)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cl_view -> {
                if (appClickListener == null) return
                appClickListener?.onClickListener(p0, p0!!.getTag() as Int)
            }
            R.id.iv_delete -> {
                if (appClickListener == null) return
                appClickListener?.onClickListener(p0, p0!!.getTag() as Int)
            }
        }
    }
}