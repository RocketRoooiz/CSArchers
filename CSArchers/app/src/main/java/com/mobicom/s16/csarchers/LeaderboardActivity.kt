package com.mobicom.s16.csarchers

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityLeaderboardBinding

class LeaderboardActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityLeaderboardBinding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}