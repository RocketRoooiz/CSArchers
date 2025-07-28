package com.mobicom.s16.csarchers.decimal_binary_sim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobicom.s16.csarchers.databinding.ActivityUdecimal2binarySimStepLayoutBinding

// Unsigned decimal to binary solution adapter:
class UD2BSolutionAdapter(private val steps: ArrayList<UnsignedDecimalToBinaryStep>): Adapter<UD2BStepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UD2BStepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ActivityUdecimal2binarySimStepLayoutBinding.inflate(inflater, parent, false)

        return UD2BStepViewHolder(view)
    }

    override fun onBindViewHolder(holder: UD2BStepViewHolder, position: Int) {
        holder.bindData(steps.get(position))
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}