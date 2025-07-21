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
            viewBinding.intArithmeticBtn.setOnClickListener {
                val intent = Intent(this, GameActivityIntArithmetic::class.java)
                startActivity(intent)
            }

            // conversion
            viewBinding.numSysBtn.setOnClickListener {
                val intent = Intent(this, GameActivityNumSys::class.java)
                startActivity(intent)
            }

            // IEEE
            viewBinding.ieeeBtn.setOnClickListener {
                val intent = Intent(this, GameActivityIEEE::class.java)
                startActivity(intent)
            }

            // UTF
            viewBinding.utfBtn.setOnClickListener {
                val intent = Intent(this, GameActivityUTF::class.java)
                startActivity(intent)
            }
        }

        if(mode == "Simulator"){
            // arithmetic
            viewBinding.intArithmeticBtn.setOnClickListener {
                val intent = Intent(this, SimulationActivity::class.java)
                startActivity(intent)
            }

            // conversion
            viewBinding.numSysBtn.setOnClickListener {
                val intent = Intent(this, SimulationActivity::class.java)
                startActivity(intent)
            }

            // IEEE
            viewBinding.ieeeBtn.setOnClickListener {
                val intent = Intent(this, SimulationActivity::class.java)
                startActivity(intent)
            }

            // UTF
            viewBinding.utfBtn.setOnClickListener {
                val intent = Intent(this, SimulationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
