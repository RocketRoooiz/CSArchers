package com.mobicom.s16.csarchers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsSimBinding
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.mobicom.s16.csarchers.binary_add_sub_sim.BinaryAddSubSimActivity
import com.mobicom.s16.csarchers.decimal_binary_sim.DecimalBinarySimActivity
import com.mobicom.s16.csarchers.floating_point_sim.HalfPrecisionFloatSimActivity
import com.mobicom.s16.csarchers.multiplier_sim.BinaryMultiplierSimActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsBinding

class TopicsSimActivity : ComponentActivity() {
    private fun showPopup(anchorView: View, message: String) {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_description, null)
        val textView = popupView.findViewById<TextView>(R.id.desc_text)
        textView.text = message

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
            isFocusable = true // Needed so it can dismiss when clicking outside
            elevation = 10f
        }

        // Show popup just *below* the anchor view
        anchorView.post {
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            // Offset below the button
            val yOffset = anchorView.height // Add some margin
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y + yOffset)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityTopicsSimBinding = ActivityTopicsSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val intent = getIntent()
        val mode = intent.getStringExtra("SIM_MODE_KEY")

        val mydbHelper = MyDbHelper(this)
        viewBinding.topicsSimLogoutBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    mydbHelper.logout()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        viewBinding.selectModeBtnLeaderboard.setOnClickListener({
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        })

        if(mode == "intarith"){
            viewBinding.intArithLl.visibility = View.VISIBLE
            viewBinding.numSysLl.visibility = View.GONE
            viewBinding.ieeeLl.visibility = View.GONE
            viewBinding.utfLl.visibility = View.GONE
        }else if(mode == "numsys"){
            viewBinding.intArithLl.visibility = View.GONE
            viewBinding.numSysLl.visibility = View.VISIBLE
            viewBinding.ieeeLl.visibility = View.GONE
            viewBinding.utfLl.visibility = View.GONE
        }else if(mode == "ieee"){
            viewBinding.intArithLl.visibility = View.GONE
            viewBinding.numSysLl.visibility = View.GONE
            viewBinding.ieeeLl.visibility = View.VISIBLE
            viewBinding.utfLl.visibility = View.GONE
        }else if(mode == "utf"){
            viewBinding.intArithLl.visibility = View.GONE
            viewBinding.numSysLl.visibility = View.GONE
            viewBinding.ieeeLl.visibility = View.GONE
        }

        // INTEGER ARITHMETIC
        viewBinding.topicsBtnAddSub.setOnClickListener {
            val intent = Intent(this, BinaryAddSubSimActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnMul.setOnClickListener {
            val intent = Intent(this, BinaryMultiplierSimActivity::class.java)
            startActivity(intent)
        }

        // NUMBERSYSTEM
        viewBinding.topicsBtnBinDec.setOnClickListener {
            val intent = Intent(this, DecimalBinarySimActivity::class.java)
            startActivity(intent)
        }

        // IEEE
        viewBinding.topicsBtnFlt16.setOnClickListener {
            val intent = Intent(this, HalfPrecisionFloatSimActivity::class.java)
            startActivity(intent)
        }

        // Show tooltips on long press
        viewBinding.topicsBtnAddSub.setOnLongClickListener {
            showPopup(it, "Perform signed and unsigned binary addition and subtraction, up to 16 bits!")
            true
        }

        viewBinding.topicsBtnMul.setOnLongClickListener {
            showPopup(it, "Perform unsigned binary multiplication.\n\n With step by step solution!")
            true
        }

        viewBinding.topicsBtnBinDec.setOnLongClickListener {
            showPopup(it, "Convert signed and unsigned decimal to binary and vice versa, up to 16 bits! \n\nWith step by step solution!")
            true
        }

        viewBinding.topicsBtnFlt16.setOnLongClickListener {
            showPopup(it, "IEEE half-precision (16-bit) floating point converter, capable of base 2 and base 10 formats!")
            true
        }

        viewBinding.topicsBtnUtf8.setOnLongClickListener {
            showPopup(it, "COMING SOON!")
            true
        }
    }
}
