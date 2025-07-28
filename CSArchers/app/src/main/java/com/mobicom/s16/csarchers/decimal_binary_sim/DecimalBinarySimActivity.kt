package com.mobicom.s16.csarchers.decimal_binary_sim

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isGone
import com.mobicom.s16.csarchers.R
import com.mobicom.s16.csarchers.Size
import com.mobicom.s16.csarchers.databinding.ActivityDbSimBinding
import com.mobicom.s16.csarchers.multiplier_sim.BinaryMultiplierSimSolutionFragment
import com.mobicom.s16.csarchers.multiplier_sim.MultiplierStep

class DecimalBinarySimActivity : AppCompatActivity() {
    companion object {
        const val BACKGROUND_TINT_COLOUR = "#E6C5A0"
    }

    private lateinit var viewBinding: ActivityDbSimBinding
    private lateinit var gestureDetector: GestureDetectorCompat

    private var converter = DecimalBinaryConverter()
    private var convert_op: (String) -> String = converter::convertDecimal2Binary
    private var conversion_bit_size = Size._8BITS
    private var are_biaries_signed = false
    private var decimal_to_binary = true

    private var is_ud2b_performed = false
    private var is_ub2d_performed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDbSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Update UI according to sharedPreferences.
        viewBinding.activityDbSimSwipeUpTv.isGone = !(is_ud2b_performed || is_ud2b_performed)
        updateStateForDataType()
        updateStateForSize()
        updateStateForMode()

        viewBinding.activityDbSimUnsignedBtn.setOnClickListener( View.OnClickListener {
            are_biaries_signed = false
            updateStateForDataType()
        })

        viewBinding.activityDbSimSignedBtn.setOnClickListener( View.OnClickListener {
            are_biaries_signed = true
            updateStateForDataType()
        })

        viewBinding.activityDbSimSize8Btn.setOnClickListener( View.OnClickListener {
            conversion_bit_size = Size._8BITS
            updateStateForSize()
        })

        viewBinding.activityDbSimSize16Btn.setOnClickListener( View.OnClickListener {
            conversion_bit_size = Size._16BITS
            updateStateForSize()
        })

        viewBinding.activityDbSimExchangeBtn.setOnClickListener( View.OnClickListener {
            decimal_to_binary = !decimal_to_binary
            updateStateForMode()
        })

        viewBinding.activityDbSimConvertBtn.setOnClickListener( View.OnClickListener {
            updateStateFromConversion()
        })

        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null || e2 == null) return false

                // Calculate vertical swipe distance and speed
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                // Check if it's a swipe-up (diffY is negative) and meets thresholds
                if (Math.abs(diffY) > 100 && Math.abs(diffY) > Math.abs(diffX) && diffY < 0) {
                    onSwipeUp()
                    return true
                }
                return false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun onSwipeUp() {
        Toast.makeText(this, "Swipe Up Detected!", Toast.LENGTH_SHORT).show()

        if (is_ud2b_performed || is_ub2d_performed) {
            val bundle = Bundle()

            if (is_ud2b_performed) {
                bundle.putString("configuration", "ud2b")
                bundle.putParcelableArrayList("steps", converter.unsigned_decimal_to_binary_steps as ArrayList<UnsignedDecimalToBinaryStep>)
            } else if (is_ub2d_performed) {
                bundle.putString("configuration", "ub2d")
                bundle.putParcelableArrayList("steps", converter.unsigned_binary_to_decimal_steps as ArrayList<UnsignedBinaryToDecimalStep>)
                bundle.putString("sum", converter.current_output)
            }

            val fragment = DecimalBinarySimSolutionFragment()
            fragment.arguments = bundle

            fragment.show(supportFragmentManager, "newTaskTag")
        }
        // Handle the swipe-up action (e.g., refresh content, show a menu, etc.)
    }

    private fun updateStateForDataType() {
        converter.isSigned = are_biaries_signed

        updateStateForInputHeader()

        if (are_biaries_signed) {
            // Update activityDbSimSignedBtn design
            viewBinding.activityDbSimUnsignedBtn.backgroundTintList = null // null means @null
            viewBinding.activityDbSimUnsignedBtn.setTextColor(resources.getColor(R.color.coffee_brown, null))

            // Update activityDbSimSignedBtn design
            viewBinding.activityDbSimSignedBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
            viewBinding.activityDbSimSignedBtn.setTextColor(resources.getColor(R.color.white, null))
        } else {
            // Update activityDbSimUnsignedBtn design
            viewBinding.activityDbSimUnsignedBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
            viewBinding.activityDbSimUnsignedBtn.setTextColor(resources.getColor(R.color.white, null))

            // Update activityDbSimSignedBtn design
            viewBinding.activityDbSimSignedBtn.backgroundTintList = null
            viewBinding.activityDbSimSignedBtn.setTextColor(resources.getColor(R.color.coffee_brown, null))
        }
    }

    private fun updateStateForSize() {
        converter.size = conversion_bit_size

        updateStateForInputHeader()

        when (conversion_bit_size) {
            Size._8BITS -> {
                viewBinding.activityDbSimSize8Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
                viewBinding.activityDbSimSize8Btn.setTextColor(resources.getColor(R.color.white, null))

                viewBinding.activityDbSimSize16Btn.backgroundTintList = null
                viewBinding.activityDbSimSize16Btn.setTextColor(resources.getColor(R.color.coffee_brown, null))

                // Disappear byte 1 (bits 15 to 8):
                viewBinding.activityDbSimByte1Ll.isGone = true
            }

            Size._16BITS -> {
                viewBinding.activityDbSimSize8Btn.backgroundTintList = null
                viewBinding.activityDbSimSize8Btn.setTextColor(resources.getColor(R.color.coffee_brown, null))

                viewBinding.activityDbSimSize16Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.coffee_brown)
                viewBinding.activityDbSimSize16Btn.setTextColor(resources.getColor(R.color.white, null))

                // Appear byte 1 (bits 15 to 8):
                viewBinding.activityDbSimByte1Ll.isGone = false
            }

            Size._32BITS -> {
                // Not to be added for now.
            }

            Size._64BITS -> {
                // Not to be added for now.
            }
        }
    }

    private fun updateStateForMode() {
        convert_op = if (decimal_to_binary) {
            resetStateForBinaryOutput() // Set all bits in binary output to 0.

            converter::convertDecimal2Binary // return value
        } else { // decimal_to_binary being false means binary to decimal.
            viewBinding.activityDbSimOutputDecimalTv.text = "0"

            converter::convertBinary2Decimal // return value
        }

        updateStateForInputHeader()

        viewBinding.activityDbSimOutputBinaryLl.isGone = !decimal_to_binary // Removes binary ll if binary to decimal. Adds it otherwise.
        viewBinding.activityDbSimOutputDecimalLl.isGone = decimal_to_binary // Removes decimal ll if decimal to binary. Adds it otherwise.
    }

    private fun updateStateFromConversion() {
        try {
            val input = viewBinding.activityDbSimInputEt.text.toString()

            val output = convert_op(input)

            if (decimal_to_binary) {
                updateStateForBinaryOutput(output)
            } else { // binary to decimal
                viewBinding.activityDbSimOutputDecimalTv.text = output
            }

            viewBinding.activityDbSimInputEt.error = null // Remove an existing error message if successful.

            is_ud2b_performed = !are_biaries_signed && decimal_to_binary
            is_ub2d_performed = !are_biaries_signed && !decimal_to_binary
        } catch (e: Exception) {
            viewBinding.activityDbSimInputEt.error = e.message

            is_ud2b_performed = false
            is_ub2d_performed = false
        }

        viewBinding.activityDbSimSwipeUpTv.isGone = !(is_ud2b_performed || is_ub2d_performed)
    }

    private fun updateStateForInputHeader() {
        val min = if (are_biaries_signed) {
            converter.signed_min
        } else { // unsigned
            converter.unsigned_min
        }

        val max = if (are_biaries_signed) {
            converter.signed_max
        } else {
            converter.unsigned_max
        }

        viewBinding.activityDbSimInputHeaderTv.text = if (decimal_to_binary) {
            "Decimal: (Range: $min - $max)"
        } else {
            "Binary: (Range: $min - $max)"
        }
    }

    private fun updateStateForBinaryOutput(output: String) {
        // Update bit 7 to 0 in binary output.
        for ((i, j) in (output.length - 8 until output.length).zip(0 until 8)) { // until excludes the right boundary.
            val view = viewBinding.activityDbSimByte0Ll.getChildAt(j)
            if (view is TextView) {
                view.text = output[i].toString()
            }
        }

        // Update bit 15 to 8 in binary output.
        if (conversion_bit_size == Size._16BITS) {
            for ((i, j) in (output.length - 16 until output.length - 8).zip(0 until 8)) { // until excludes the right boundary.
                val view = viewBinding.activityDbSimByte1Ll.getChildAt(j)
                if (view is TextView) {
                    view.text = output[i].toString()
                }
            }
        }

        // Implementation for other bit sizes not to be added for now.
    }

    private fun resetStateForBinaryOutput() {
        for (i in 0 until viewBinding.activityDbSimByte0Ll.childCount) {
            val view = viewBinding.activityDbSimByte0Ll.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }

        for (i in 0 until viewBinding.activityDbSimByte1Ll.childCount) {
            val view = viewBinding.activityDbSimByte1Ll.getChildAt(i)
            if (view is TextView) {
                view.text = "0"
            }
        }
    }
}