package com.mobicom.s16.csarchers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsSimBinding
import android.view.View
import com.mobicom.s16.csarchers.binary_add_sub_sim.BinaryAddSubSimActivity
import com.mobicom.s16.csarchers.decimal_binary_sim.DecimalBinarySimActivity
import com.mobicom.s16.csarchers.floating_point_sim.HalfPrecisionFloatSimActivity
import com.mobicom.s16.csarchers.multiplier_sim.BinaryMultiplierSimActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsBinding

class TopicsSimActivity : ComponentActivity() {
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
    }
}
