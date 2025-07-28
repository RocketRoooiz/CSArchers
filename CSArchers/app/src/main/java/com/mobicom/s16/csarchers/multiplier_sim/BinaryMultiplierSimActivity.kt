package com.mobicom.s16.csarchers.multiplier_sim

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.ComponentActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isGone

import com.mobicom.s16.csarchers.Size
import com.mobicom.s16.csarchers.databinding.ActivityBmSimBinding


class BinaryMultiplierSimActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityBmSimBinding
    private lateinit var gestureDetector: GestureDetectorCompat

    private var multiplier = BinaryMultiplier()
    private var multiplication_size = Size._8BITS
    private var is_calculated = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBmSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Update UI according to sharedPreferences.
        resetStateForBinaryOutput()
        viewBinding.activityBmSimSwipeUpTv.isGone = !is_calculated

        viewBinding.activityBmSimCalculateBtn.setOnClickListener( View.OnClickListener {
            updateStateFromOperation()
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
        if (is_calculated) {
            val bundle = Bundle()
            bundle.putInt("size", multiplier.size.value)
            bundle.putLong("m", multiplier.constant_m.toLong())
            bundle.putParcelableArrayList("steps", multiplier.steps as ArrayList<MultiplierStep>)

            val fragment = BinaryMultiplierSimSolutionFragment()
            fragment.arguments = bundle

            fragment.show(supportFragmentManager, "newTaskTag")
        }
        // Handle the swipe-up action (e.g., refresh content, show a menu, etc.)
    }

    private fun updateStateFromOperation() {
        try {
            val input1 = viewBinding.activityBmSimMultiplicandEt.text.toString()
            val input2 = viewBinding.activityBmSimMultiplierEt.text.toString()

            val output = multiplier.operate(input1, input2)

            updateStateForBinaryOutput(output)

            is_calculated = true;

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

        viewBinding.activityBmSimSwipeUpTv.isGone = !is_calculated
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

