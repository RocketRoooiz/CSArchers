package com.mobicom.s16.csarchers.multiplier_sim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobicom.s16.csarchers.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobicom.s16.csarchers.databinding.ActivityBmSimBinding
import com.mobicom.s16.csarchers.databinding.FragmentActivityBmDbcSimSolutionBinding

class BinaryMultiplierSimSolutionFragment : BottomSheetDialogFragment() {
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

        val size = requireArguments().getInt("size")
        val m = requireArguments().getLong("m").toULong()
        val steps = requireArguments().getParcelableArrayList<MultiplierStep>("steps")

        viewBinding.activityBmDbcUd2bHeaderLl.isGone = true
        viewBinding.activityBmDbcUb2dHeaderLl.isGone = true
        viewBinding.activityBmDbcSumTv.isGone = true

        viewBinding.activityBmDbcSimSolutionMTv.text = "M: ${m.toString(2).padStart(size, '0')}"
        viewBinding.activityBmDbcSimStepsRv.adapter = SolutionAdapter(steps!!, size)
        viewBinding.activityBmDbcSimStepsRv.layoutManager = LinearLayoutManager(this.context)

        viewBinding.activityBmDbcSimSolutionBackBtn.setOnClickListener(View.OnClickListener {
            requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        })
    }
}