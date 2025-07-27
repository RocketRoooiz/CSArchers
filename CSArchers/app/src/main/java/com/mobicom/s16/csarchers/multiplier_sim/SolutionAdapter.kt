package com.mobicom.s16.csarchers.multiplier_sim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobicom.s16.csarchers.databinding.ActivityBmSimStepLayoutBinding

class SolutionAdapter(private val steps: ArrayList<MultiplierStep>): Adapter<StepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ActivityBmSimStepLayoutBinding.inflate(inflater, parent, false)

        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bindData(steps.get(position))
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}