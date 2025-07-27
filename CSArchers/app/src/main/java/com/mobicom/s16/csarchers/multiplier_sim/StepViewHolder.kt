package com.mobicom.s16.csarchers.multiplier_sim

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobicom.s16.csarchers.databinding.ActivityBmSimStepLayoutBinding

class StepViewHolder(private val stepLayoutBinding: ActivityBmSimStepLayoutBinding)
    : ViewHolder(stepLayoutBinding.root) {

    fun bindData(multiplierStep: MultiplierStep) {
        stepLayoutBinding.activityBmSimStepTv.text = "Step ${multiplierStep.bit}:"
        stepLayoutBinding.activityBmSimStepInitialATv.text = multiplierStep.a.toString(2).padStart(8, '0')
        stepLayoutBinding.activityBmSimStepInitialQTv.text = multiplierStep.q.toString(2).padStart(8, '0')


        stepLayoutBinding.activityBmSimStepQCheckTv.text = if (multiplierStep.add_m) {
            "Q0 = 1: Add M"
        } else {
            "Q0 = 0: "
        }
        stepLayoutBinding.activityBmSimStepQCheckQTv.text = "C = ${if (multiplierStep.carry) "1" else "0"}"
        stepLayoutBinding.activityBmSimStepQCheckATv.text = multiplierStep.a_plus_m.toString(2).padStart(8, '0')
        stepLayoutBinding.activityBmSimStepQCheckQTv.text = multiplierStep.q.toString(2).padStart(8, '0')

        stepLayoutBinding.activityBmSimStepShrCTv.text = "C = 0"
        stepLayoutBinding.activityBmSimStepShrATv.text = multiplierStep.a_shr.toString(2).padStart(8, '0')
        stepLayoutBinding.activityBmSimStepShrQTv.text = multiplierStep.q_shr.toString(2).padStart(8, '0')
    }
}