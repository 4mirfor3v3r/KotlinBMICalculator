package com.amier.bmicalculator

import com.amier.bmicalculator.sqlite.Data
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.main_item.view.*

class MainItem(private val data: Data):Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.main_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.main_item_day.text = data.day
        viewHolder.itemView.main_item_date.text = data.date
        viewHolder.itemView.main_item_weight.text = data.weight
        viewHolder.itemView.main_item_bmi.text = data.bmi
        viewHolder.itemView.main_item_div.text = data.div

        if(data.icon == "0"){
            viewHolder.itemView.main_item_icon.setImageResource(R.mipmap.ic_down)
        }else if(data.icon == "1"){
            viewHolder.itemView.main_item_icon.setImageResource(R.mipmap.ic_up)
        }
    }

}