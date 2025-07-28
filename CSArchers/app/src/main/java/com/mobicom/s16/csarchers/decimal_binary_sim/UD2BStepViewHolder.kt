package com.mobicom.s16.csarchers.decimal_binary_sim

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobicom.s16.csarchers.databinding.ActivityBmSimStepLayoutBinding
import com.mobicom.s16.csarchers.databinding.ActivityUdecimal2binarySimStepLayoutBinding

// Unsigned decimal to binary step view holder:
class UD2BStepViewHolder(private val stepLayoutBinding: ActivityUdecimal2binarySimStepLayoutBinding)
    : ViewHolder(stepLayoutBinding.root) {

    fun bindData(ud2b_step: UnsignedDecimalToBinaryStep) {
        stepLayoutBinding.activityUd2bSimStepDividendTv.text = ud2b_step.dividend.toString()
        stepLayoutBinding.activityUd2bSimStepQuotientTv.text = ud2b_step.quotient.toString()
        stepLayoutBinding.activityUd2bSimStepRemainderTv.text = ud2b_step.remainder.toString()
        stepLayoutBinding.activityUd2bSimStepBitTv.text = ud2b_step.bit.toString()
    }
}