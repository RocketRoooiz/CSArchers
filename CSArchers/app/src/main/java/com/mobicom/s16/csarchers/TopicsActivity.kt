package com.mobicom.s16.csarchers


import android.app.AlertDialog
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

        val mydbHelper = MyDbHelper(this)
        viewBinding.topicsLogoutBtn.setOnClickListener {
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
    }
}
