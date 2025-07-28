package com.mobicom.s16.csarchers.decimal_binary_sim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobicom.s16.csarchers.databinding.FragmentActivityBmDbcSimSolutionBinding
import com.mobicom.s16.csarchers.multiplier_sim.MultiplierStep

class DecimalBinarySimSolutionFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentActivityBmDbcSimSolutionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentActivityBmDbcSimSolutionBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the arguments from the bundle.
        val configuration = requireArguments().getString("configuration")
        val steps = when (configuration) {
            "ud2b" -> requireArguments().getParcelableArrayList<UnsignedDecimalToBinaryStep>("steps")
            "ub2d" -> requireArguments().getParcelableArrayList<UnsignedBinaryToDecimalStep>("steps")
            else -> throw IllegalArgumentException("The configuration should currently be either ud2b or ub2d.")
        }

        // Views above the recycler view are shown or hidden depending on the converter configuration.
        viewBinding.activityBmDbcSimSolutionMTv.isGone = true
        viewBinding.activityBmDbcUd2bHeaderLl.isGone = configuration != "ud2b"
        viewBinding.activityBmDbcUb2dHeaderLl.isGone = configuration != "ub2d"
        viewBinding.activityBmDbcSumTv.isGone = configuration != "ub2d"

        // Set up steps recycler view depending on the converter configuration.
        when (configuration) {
            "ud2b" -> viewBinding.activityBmDbcSimStepsRv.adapter = UD2BSolutionAdapter(steps as ArrayList<UnsignedDecimalToBinaryStep>)
            "ub2d" ->  viewBinding.activityBmDbcSimStepsRv.adapter = UB2DSolutionAdapter(steps as ArrayList<UnsignedBinaryToDecimalStep>)
        }
        viewBinding.activityBmDbcSimStepsRv.layoutManager = LinearLayoutManager(this.context)

        if (configuration == "ub2d") { // Write the sum if the converter configuration is unsigned and binary to decimal.
            val sum = requireArguments().getString("sum")
            viewBinding.activityBmDbcSumTv.text = "Sum: $sum"
        }

        // Back button set to return to the activity.
        viewBinding.activityBmDbcSimSolutionBackBtn.setOnClickListener(View.OnClickListener {
            requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        })
    }
}