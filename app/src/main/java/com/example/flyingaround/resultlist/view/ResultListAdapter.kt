package com.example.flyingaround.resultlist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingaround.R
import kotlinx.android.synthetic.main.item_result_list.view.*

class ResultListAdapter(var flightInfoItemList: List<FlightInfoItem>) :
    RecyclerView.Adapter<ResultListAdapter.ViewHolder>() {

    var onClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view, onClickListener)
    }

    override fun getItemCount(): Int = flightInfoItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(flightInfoItemList[position])
    }

    override fun getItemViewType(position: Int) = R.layout.item_result_list

    inner class ViewHolder(itemView: View, private val onClickListener: ((String) -> Unit)? = null) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FlightInfoItem) {
            itemView.price.text = "${item.price} ${item.currency}"
            itemView.flightDate.text = item.flightDate
            itemView.flightNumber.text = item.flightNumber
            itemView.duration.text = item.duration
            itemView.setOnClickListener { onClickListener?.invoke(item.flightNumber) }
        }
    }
}
