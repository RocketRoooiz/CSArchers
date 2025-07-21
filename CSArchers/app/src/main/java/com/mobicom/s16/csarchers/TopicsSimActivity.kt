package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsSimBinding
import android.view.View

class TopicsSimActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityTopicsSimBinding = ActivityTopicsSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val intent = getIntent()
        val mode = intent.getStringExtra("SIM_MODE_KEY")

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
            viewBinding.utfLl.visibility = View.VISIBLE
        }

        // INTEGER ARITHMETIC
        viewBinding.topicsBtnAdd.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnMult.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnSub.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnDiv.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }

        // NUMBERSYSTEM
        viewBinding.topicsBtnBinDec.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnBinHex.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnBinOct.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnDecOct.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }

        // IEEE
        viewBinding.topicsBtnFlt32.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnDec32.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnFlt64.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnDec64.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }

        // UTF
        viewBinding.topicsBtnUtf8.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnUtf16.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
        viewBinding.topicsBtnUtf32.setOnClickListener {
            val intent = Intent(this, SimulationActivity::class.java)
            startActivity(intent)
        }
    }
}
