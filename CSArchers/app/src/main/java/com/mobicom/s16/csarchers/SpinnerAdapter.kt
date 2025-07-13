package com.mobicom.s16.csarchers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.mobicom.s16.csarchers.databinding.ActivityBasSpinnerItemLayoutBinding

class SpinnerAdapter(private val context: Context, private val items: List<SpinnerItem>): BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): SpinnerItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val inflater = LayoutInflater.from(context)
        val viewBinding = ActivityBasSpinnerItemLayoutBinding.inflate(inflater, parent, false)

        val item = getItem(position)

        viewBinding.activityBasSpinnerItemLayoutImg.setImageResource(item.iconResId)

        return viewBinding.root
    }
}