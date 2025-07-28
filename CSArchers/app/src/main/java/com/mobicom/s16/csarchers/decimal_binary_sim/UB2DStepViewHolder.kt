package com.mobicom.s16.csarchers.decimal_binary_sim

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobicom.s16.csarchers.databinding.ActivityUbinary2decimalSimStepLayoutBinding
import com.mobicom.s16.csarchers.databinding.ActivityUdecimal2binarySimStepLayoutBinding

class UB2DStepViewHolder(private val stepLayoutBinding: ActivityUbinary2decimalSimStepLayoutBinding)
    : ViewHolder(stepLayoutBinding.root) {

    fun bindData(ub2d_step: UnsignedBinaryToDecimalStep) {
        stepLayoutBinding.activityUb2dSimStepBitTv.text = ub2d_step.bit.toString()
        stepLayoutBinding.activityUb2dSimStepPowerOfTwoTv.text = ub2d_step.powerOfTwo .toString()
        stepLayoutBinding.activityUb2dSimStepProductTv.text = ub2d_step.product.toString()
    }
}