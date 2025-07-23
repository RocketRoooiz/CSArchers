package com.mobicom.s16.csarchers.multiplier_sim

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity

import com.mobicom.s16.csarchers.Size
import com.mobicom.s16.csarchers.databinding.ActivityBmSimBinding


class BinaryMultiplierSimActivity : ComponentActivity() {
    private lateinit var viewBinding: ActivityBmSimBinding

    private var multiplier = BinaryMultiplier()
    private var multiplication_size = Size._8BITS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBmSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Update UI according to sharedPreferences.
        resetStateForBinaryOutput()

        viewBinding.activityBmSimCalculateBtn.setOnClickListener( View.OnClickListener {
            updateStateFromOperation()
        })
    }

    private fun updateStateFromOperation() {
        try {
            val input1 = viewBinding.activityBmSimMultiplicandEt.text.toString()
            val input2 = viewBinding.activityBmSimMultiplierEt.text.toString()

            val output = multiplier.operate(input1, input2)

            updateStateForBinaryOutput(output)

            viewBinding.activityBmSimMultiplicandEt.error = null // Remove an existing error message for input 1 if successful.
            viewBinding.activityBmSimMultiplierEt.error = null // Remove an existing error message for input 2 if successful.
        } catch (e: Exception) {
            viewBinding.activityBmSimMultiplicandEt.error = e.message
            viewBinding.activityBmSimMultiplierEt.error = e.message

            if (e.message!!.contains(Regex("multiplicand", RegexOption.IGNORE_CASE))) {
                viewBinding.activityBmSimMultiplierEt.error = null
            }

            if (e.message!!.contains(Regex("multiplier", RegexOption.IGNORE_CASE))) {
                viewBinding.activityBmSimMultiplicandEt.error = null
            }
        }
    }

    private fun updateStateForBinaryOutput(output: String) {
        // Update bit 7 to 0 in binary output.
        for ((i, j) in (output.length - 8 until output.length).zip(0 until 8)) { // until excludes the right boundary.
            val view = viewBinding.activityBmSimByte0Ll.getChildAt(j)
            if (view is TextView) {
                view.text = output[i].toString()
            }
        }

        // Update bit 15 to 8 in binary output.
        for ((i, j) in (output.length - 16 until output.length - 8).zip(0 until 8)) { // until excludes the right boundary.
            val view = viewBinding.activityBmSimByte1Ll.getChildAt(j)
            if (view is TextView) {
                view.text = output[i].toString()
            }
        }


        // Implementation for other bit sizes not to be added for now.
    }

    private fun resetStateForBinaryOutput() {
        for (i in 0 until viewBinding.activityBmSimByte0Ll.childCount) {
            val view = viewBinding.activityBmSimByte0Ll.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }

        for (i in 0 until viewBinding.activityBmSimByte1Ll.childCount) {
            val view = viewBinding.activityBmSimByte1Ll.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }
    }
}

