package com.wsyapp.ui.leftmenu.profile.cars.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.data.model.response.CarModel

class CarAdapter(var context: Context, val carList: List<CarModel>?) :
    RecyclerView.Adapter<CarAdapter.ViewHolder>(), View.OnClickListener {

    private lateinit var listener: AppClickListener

    fun setAppClickListener(listener: AppClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cl_view: ConstraintLayout = itemView.findViewById(R.id.cl_view)
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val tv_car_plate_number: TextView = itemView.findViewById(R.id.tv_car_plate_number)
        val tv_model: TextView = itemView.findViewById(R.id.tv_model)
        val sc_select: SwitchCompat = itemView.findViewById(R.id.sc_select)
        val iv_car_img: ImageView = itemView.findViewById(R.id.iv_car_img)
        val iv_edit_car: ImageView = itemView.findViewById(R.id.iv_edit_car)
        val iv_delete: ImageView = itemView.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = carList?.get(position)
        if (model != null) {
            holder.tv_title.setText(model.name)
            holder.tv_car_plate_number.setText(model.plate)
            holder.tv_model.setText(model.model)
            holder.tv_title.setText(model.name)
            Glide.with(context).load(model.car).placeholder(R.drawable.bg_image_placeholder).into(holder.iv_car_img)
            var isdefault = model?.isdefault ?: false
            if (isdefault) {
                holder.sc_select.setChecked(true)
            } else {
                holder.sc_select.setChecked(false)
            }
            holder.iv_edit_car.setTag(position)
            holder.iv_delete.setTag(position)
            holder.iv_edit_car.setOnClickListener(this)
            holder.iv_delete.setOnClickListener(this)
        }
    }

    public interface AppClickListener {
        fun onClickListener(view: View, position: Int)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_edit_car, R.id.iv_delete -> {
                if (listener == null) return
                listener.onClickListener(p0, p0!!.getTag() as Int)
            }
        }
    }

}