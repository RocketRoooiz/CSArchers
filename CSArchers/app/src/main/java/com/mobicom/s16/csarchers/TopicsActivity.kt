package com.mobicom.s16.csarchers


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsBinding


class TopicsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityTopicsBinding = ActivityTopicsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val intent = getIntent()
        val mode = intent.getStringExtra("Mode_Key")

        viewBinding.selectModeBtnLeaderboard.setOnClickListener({
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        })

        if(mode == "Game"){
            // arithmetic
            viewBinding.topicsBtnAdd.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnMult.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnSub.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnDiv.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }

            // conversion
            viewBinding.topicsBtnBinDec.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnBinHex.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnBinOct.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnDecOct.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }

            // IEEE
            viewBinding.topicsBtnFlt32.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnDec32.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnFlt64.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnDec64.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }

            // UTF
            viewBinding.topicsBtnUtf8.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnUtf16.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            viewBinding.topicsBtnUtf32.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }

        if(mode == "Simulator"){
            // arithmetic
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

            // conversion
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
}
