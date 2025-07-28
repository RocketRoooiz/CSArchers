package com.mobicom.s16.csarchers.multiplier_sim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobicom.s16.csarchers.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobicom.s16.csarchers.databinding.ActivityBmSimBinding
import com.mobicom.s16.csarchers.databinding.FragmentActivityBmSimSolutionBinding

class BinaryMultiplierSimSolutionFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentActivityBmSimSolutionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentActivityBmSimSolutionBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val size = requireArguments().getInt("size")
        val m = requireArguments().getLong("m").toULong()
        val steps = requireArguments().getParcelableArrayList<MultiplierStep>("steps")

        viewBinding.activityBmSimSolutionSheetMTv.text = "M: ${m.toString(2).padStart(size, '0')}"
        viewBinding.activityBmSimStepsRv.adapter = SolutionAdapter(steps!!, size)
        viewBinding.activityBmSimStepsRv.layoutManager = LinearLayoutManager(this.context)

        viewBinding.activityBmSimSolutionSheetBackBtn.setOnClickListener(View.OnClickListener {
            requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        })
    }
}