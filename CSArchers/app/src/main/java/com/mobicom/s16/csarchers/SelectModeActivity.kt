package com.mobicom.s16.csarchers


import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.core.app.ActivityCompat
import com.mobicom.s16.csarchers.databinding.ActivitySelectModeBinding
import com.mobicom.s16.csarchers.notification_senders.AlarmScheduler
import com.mobicom.s16.csarchers.notification_senders.NotificationHelper

class SelectModeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivitySelectModeBinding = ActivitySelectModeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        AlarmScheduler.scheduleWeeklyAlarm(this)


        viewBinding.selectModeTvWelcome.text = intent.getStringExtra(IntentKeys.USER_NAME_KEY.name
        )
        viewBinding.selectModeBtnGame.setOnClickListener({
            val intent = Intent(this, TopicsActivity::class.java)
            intent.putExtra("Mode_Key", "Game")
            startActivity(intent)
        })

        viewBinding.selectModeBtnSim.setOnClickListener({
            val intent = Intent(this, TopicsActivity::class.java)
            intent.putExtra("Mode_Key", "Simulator")
            startActivity(intent)
        })

        viewBinding.selectModeBtnLeaderboard.setOnClickListener({
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        })

        val mydbHelper = MyDbHelper(this)
        viewBinding.selectModeLogoutBtn.setOnClickListener {
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