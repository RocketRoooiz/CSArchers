package com.mobicom.s16.csarchers.floating_point_sim

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.mobicom.s16.csarchers.R
import com.mobicom.s16.csarchers.Size
import com.mobicom.s16.csarchers.databinding.ActivityDbSimBinding
import com.mobicom.s16.csarchers.databinding.ActivityHpfcSimBinding
import com.mobicom.s16.csarchers.decimal_binary_sim.DecimalBinaryConverter
import com.mobicom.s16.csarchers.floating_point_sim.HalfPrecisionFloatConverter

class HalfPrecisionFloatSimActivity : ComponentActivity() {
    companion object {
        const val BACKGROUND_TINT_COLOUR = "#E6C5A0"
    }

    private lateinit var viewBinding: ActivityHpfcSimBinding

    private var converter = HalfPrecisionFloatConverter()
    private var usesBase10 = false // false means using base 2 for input.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHpfcSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Update UI according to sharedPreferences.
        updateStateForBase()
        resetStateForBinaryOutput()

        viewBinding.activityHpfcSimBase2Btn.setOnClickListener( View.OnClickListener {
            usesBase10 = false
            updateStateForBase()
        })

        viewBinding.activityHpfcSimBase10Btn.setOnClickListener( View.OnClickListener {
            usesBase10 = true
            updateStateForBase()
        })

        viewBinding.activityHpfcSimConvertBtn.setOnClickListener( View.OnClickListener {
            updateStateFromConversion()
        })
    }

    private fun updateStateForBase() {
        if (usesBase10) {
            // Update activityDbSimBase2Btn design
            viewBinding.activityHpfcSimBase2Btn.backgroundTintList = null // null means @null
            viewBinding.activityHpfcSimBase2Btn.setTextColor(resources.getColor(R.color.coffee_brown, null))

            // Update activityDbSimBase10Btn design
            viewBinding.activityHpfcSimBase10Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
            viewBinding.activityHpfcSimBase10Btn.setTextColor(resources.getColor(R.color.white, null))
        } else { // uses base 10.
            // Update activityDbSimBase2Btn design
            viewBinding.activityHpfcSimBase2Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
            viewBinding.activityHpfcSimBase2Btn.setTextColor(resources.getColor(R.color.white, null))

            // Update activityDbSimBase10Btn design
            viewBinding.activityHpfcSimBase10Btn.backgroundTintList = null
            viewBinding.activityHpfcSimBase10Btn.setTextColor(resources.getColor(R.color.coffee_brown, null))
        }
    }

    private fun updateStateFromConversion() {
        val mantissa_input = viewBinding.activityHpfcSimMantissaInputEt.text.toString()
        val exponent_input = viewBinding.activityHpfcSimExponentInputEt.text.toString()
        val output = converter.convert(mantissa_input, exponent_input, usesBase10)

        updateStateForBinaryOutput(output)
    }


    private fun updateStateForBinaryOutput(output: HalfPrecisionFloatConverter.ConversionResult) {
        val binary_output = output.binary
        val message_output = output.message

        // Handles binary output UI
        viewBinding.activityHpfcSimBit15Tv.text = binary_output[0].toString() // Updates bit for sign (bit 15).

        // Updates bits for mantissa (bits 14 to 10).
        for (i in 0 until viewBinding.activityHpfcSimMantissaBinaryOutputBitsLl.childCount) {
            val view = viewBinding.activityHpfcSimMantissaBinaryOutputBitsLl.getChildAt(i)
            if (view is TextView) {
                view.text = binary_output[i + 2].toString()
            }
        }

        // Updates bits for exponent (bits 9 to 0).
        for (i in 0 until viewBinding.activityHpfcSimExponentBinaryOutputBitsLl.childCount) {
            val view = viewBinding.activityHpfcSimExponentBinaryOutputBitsLl.getChildAt(i)
            if (view is TextView) {
                view.text = binary_output[i + 8].toString()
            }
        }

        // Handles output messaging in header.
         viewBinding.activityHpfcSimBinaryOutputHeaderTv.text = "Result: $message_output"
    }

    private fun resetStateForBinaryOutput() {
        viewBinding.activityHpfcSimBit15Tv.text = "0" // Resets bit 15 which is the sign bit to 0.

        for (i in 0 until viewBinding.activityHpfcSimMantissaBinaryOutputBitsLl.childCount) {
            val view = viewBinding.activityHpfcSimMantissaBinaryOutputBitsLl.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }

        for (i in 0 until viewBinding.activityHpfcSimExponentBinaryOutputBitsLl.childCount) {
            val view = viewBinding.activityHpfcSimExponentBinaryOutputBitsLl.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }
    }
}