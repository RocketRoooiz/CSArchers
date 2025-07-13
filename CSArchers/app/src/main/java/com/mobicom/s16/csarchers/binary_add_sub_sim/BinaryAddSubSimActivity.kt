package com.mobicom.s16.csarchers.binary_add_sub_sim

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.mobicom.s16.csarchers.R
import com.mobicom.s16.csarchers.Size
import com.mobicom.s16.csarchers.databinding.ActivityBasSimBinding

class BinaryAddSubSimActivity: ComponentActivity() {
    private lateinit var viewBinding: ActivityBasSimBinding

    private var adder_subractor = BinaryAdderSubtractor()
    private var operation = OpType.ADD
    private var operation_bit_size = Size._8BITS
    private var are_biaries_signed = false
    private var isOutputOverflowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBasSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        updateStateForDataType()
        updateStateForSize()
        updateStateForOpType()
        updateStateForOverflow()

        val spinner_items = listOf(
            SpinnerItem(R.drawable.plus_icon, "Addition", OpType.ADD),
            SpinnerItem(R.drawable.minus_icon, "Subtraction", OpType.SUBTRACT)
        )

        val spinner_adapter = SpinnerAdapter(this, spinner_items)
        viewBinding.activityBasSimOpSpn.adapter = spinner_adapter
        viewBinding.activityBasSimOpSpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinner_items[position]
                // Show a toast message with the selected item's name
                Toast.makeText(this@BinaryAddSubSimActivity, "Selected operation: ${selectedItem.itemName}", Toast.LENGTH_SHORT).show()
                operation = selectedItem.op
                updateStateForOpType()
                updateStateForOverflow()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected if needed
            }
        }

        viewBinding.activityBasSimUnsignedBtn.setOnClickListener( View.OnClickListener {
            are_biaries_signed = false
            updateStateForDataType()
            updateStateForOverflow()
        })

        viewBinding.activityBasSimSignedBtn.setOnClickListener( View.OnClickListener {
            are_biaries_signed = true
            updateStateForDataType()
            updateStateForOverflow()
        })

        viewBinding.activityBasSimSize8Btn.setOnClickListener( View.OnClickListener {
            operation_bit_size = Size._8BITS
            updateStateForSize()
            updateStateForOverflow()
        })

        viewBinding.activityBasSimSize16Btn.setOnClickListener( View.OnClickListener {
            operation_bit_size = Size._16BITS
            updateStateForSize()
            updateStateForOverflow()
        })

        viewBinding.activityBasSimCalculateBtn.setOnClickListener( View.OnClickListener {
            updateStateFromOperation()
        })
    }

    private fun updateStateForDataType() {
        adder_subractor.isSigned = are_biaries_signed

        updateStateForInputHeader()
        resetStateforBinaryInputs()
        resetStateForBinaryOutput()

        if (are_biaries_signed) {
            // Update activityDbSimSignedBtn design
            viewBinding.activityBasSimUnsignedBtn.backgroundTintList = null // null means @null
            viewBinding.activityBasSimUnsignedBtn.setTextColor(resources.getColor(R.color.coffee_brown, null))

            // Update activityDbSimSignedBtn design
            viewBinding.activityBasSimSignedBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
            viewBinding.activityBasSimSignedBtn.setTextColor(resources.getColor(R.color.white, null))
        } else {
            // Update activityDbSimUnsignedBtn design
            viewBinding.activityBasSimUnsignedBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
            viewBinding.activityBasSimUnsignedBtn.setTextColor(resources.getColor(R.color.white, null))

            // Update activityDbSimSignedBtn design
            viewBinding.activityBasSimSignedBtn.backgroundTintList = null
            viewBinding.activityBasSimSignedBtn.setTextColor(resources.getColor(R.color.coffee_brown, null))
        }
    }

    private fun updateStateForSize() {
        adder_subractor.size = operation_bit_size

        updateStateForInputHeader()
        resetStateforBinaryInputs()
        resetStateForBinaryOutput()

        when (operation_bit_size) {
            Size._8BITS -> {
                viewBinding.activityBasSimSize8Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
                viewBinding.activityBasSimSize8Btn.setTextColor(resources.getColor(R.color.white, null))

                viewBinding.activityBasSimSize16Btn.backgroundTintList = null
                viewBinding.activityBasSimSize16Btn.setTextColor(resources.getColor(R.color.coffee_brown, null))

                // Disappear byte 1 (bits 15 to 8):
                viewBinding.activityBasSimByte1Ll.isGone = true
            }

            Size._16BITS -> {
                viewBinding.activityBasSimSize8Btn.backgroundTintList = null
                viewBinding.activityBasSimSize8Btn.setTextColor(resources.getColor(R.color.coffee_brown, null))

                viewBinding.activityBasSimSize16Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
                viewBinding.activityBasSimSize16Btn.setTextColor(resources.getColor(R.color.white, null))

                // Appear byte 1 (bits 15 to 8):
                viewBinding.activityBasSimByte1Ll.isGone = false
            }

            Size._32BITS -> {
                // Not to be added for now.
            }

            Size._64BITS -> {
                // Not to be added for now.
            }
        }
    }

    private fun updateStateForOpType() {
        adder_subractor.operation = operation

        resetStateforBinaryInputs()
        resetStateForBinaryOutput()
    }

    private fun updateStateForOverflow() {
        if (isOutputOverflowed) {
            viewBinding.activityBasSimBinaryOutputHeaderTv.text = "Result (Overflowed):"
            viewBinding.activityBasSimBinaryOutputHeaderTv.setBackgroundColor(resources.getColor(R.color.red, null))
            viewBinding.activityBasSimBinaryOutputHeaderTv.setTextColor(resources.getColor(R.color.white, null))
        } else {
            viewBinding.activityBasSimBinaryOutputHeaderTv.text = "Result:"
            viewBinding.activityBasSimBinaryOutputHeaderTv.background = null
            viewBinding.activityBasSimBinaryOutputHeaderTv.setTextColor(resources.getColor(R.color.coffee_brown, null))
        }
    }

    private fun updateStateFromOperation() {
        try {
            val input1 = viewBinding.activityBasSimInput1Et.text.toString()
            val input2 = viewBinding.activityBasSimInput2Et.text.toString()

            val output = adder_subractor.operate(input1, input2)
            isOutputOverflowed = adder_subractor.isOverflowed

            updateStateForBinaryOutput(output)
            updateStateForOverflow()

            viewBinding.activityBasSimInput1Et.error = null // Remove an existing error message for input 1 if successful.
            viewBinding.activityBasSimInput2Et.error = null // Remove an existing error message for input 2 if successful.
        } catch (e: Exception) {
            viewBinding.activityBasSimInput1Et.error = e.message
            viewBinding.activityBasSimInput2Et.error = e.message

            if (e.message!!.contains(Regex("input 1"))) {
                viewBinding.activityBasSimInput2Et.error = null
            }

            if (e.message!!.contains(Regex("input 2"))) {
                viewBinding.activityBasSimInput1Et.error = null
            }
        }
    }

    private fun updateStateForInputHeader() {
        val min = if (are_biaries_signed) {
            adder_subractor.signed_min
        } else { // unsigned
            adder_subractor.unsigned_min
        }

        val max = if (are_biaries_signed) {
            adder_subractor.signed_max
        } else {
            adder_subractor.unsigned_max
        }

        viewBinding.activityBasSimInput1HeaderTv.text = "Input 1: (Range: $min - $max)"
        viewBinding.activityBasSimInput2HeaderTv.text = "Input 2: (Range: $min - $max)"

    }

    private fun updateStateForBinaryOutput(output: String) {
        // Update bit 7 to 0 in binary output.
        for ((i, j) in (output.length - 8 until output.length).zip(0 until 8)) { // until excludes the right boundary.
            val view = viewBinding.activityBasSimByte0Ll.getChildAt(j)
            if (view is TextView) {
                view.text = output[i].toString()
            }
        }

        // Update bit 15 to 8 in binary output.
        if (operation_bit_size == Size._16BITS) {
            for ((i, j) in (output.length - 16 until output.length - 8).zip(0 until 8)) { // until excludes the right boundary.
                val view = viewBinding.activityBasSimByte1Ll.getChildAt(j)
                if (view is TextView) {
                    view.text = output[i].toString()
                }
            }
        }

        // Implementation for other bit sizes not to be added for now.
    }

    private fun resetStateforBinaryInputs() {
        when (operation_bit_size) {
            Size._8BITS -> {
                viewBinding.activityBasSimInput1Et.setText("00000000")
                viewBinding.activityBasSimInput2Et.setText("00000000")
            }

            Size._16BITS -> {
                viewBinding.activityBasSimInput1Et.setText("0000000000000000")
                viewBinding.activityBasSimInput2Et.setText("0000000000000000")
            }

            Size._32BITS -> {
                // Not to be added for now.
            }

            Size._64BITS -> {
                // Not to be added for now.
            }
        }
    }

    private fun resetStateForBinaryOutput() {
        for (i in 0 until viewBinding.activityBasSimByte0Ll.childCount) {
            val view = viewBinding.activityBasSimByte0Ll.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }

        for (i in 0 until viewBinding.activityBasSimByte1Ll.childCount) {
            val view = viewBinding.activityBasSimByte1Ll.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }
    }
}