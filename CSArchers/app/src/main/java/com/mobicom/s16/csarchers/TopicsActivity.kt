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

            }

            // conversion
            viewBinding.numSysBtn.setOnClickListener {

            }

            // IEEE
            viewBinding.ieeeBtn.setOnClickListener {

            }

            // UTF
            viewBinding.utfBtn.setOnClickListener {

            }
        }

        if(mode == "Simulator"){
            // arithmetic
            viewBinding.intArithmeticBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "intarith")
                startActivity(intent)
            }

            // conversion
            viewBinding.numSysBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "numsys")
                startActivity(intent)
            }

            // IEEE
            viewBinding.ieeeBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "ieee")
                startActivity(intent)
            }

            // UTF
            viewBinding.utfBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "utf")
                startActivity(intent)
            }
        }
    }
}
