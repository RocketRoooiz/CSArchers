package com.mobicom.s16.csarchers.decimal_binary_sim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobicom.s16.csarchers.databinding.ActivityUbinary2decimalSimStepLayoutBinding

class UB2DSolutionAdapter(private val steps: ArrayList<UnsignedBinaryToDecimalStep>): Adapter<UB2DStepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UB2DStepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ActivityUbinary2decimalSimStepLayoutBinding.inflate(inflater, parent, false)

        return UB2DStepViewHolder(view)
    }

    override fun onBindViewHolder(holder: UB2DStepViewHolder, position: Int) {
        holder.bindData(steps.get(position))
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}