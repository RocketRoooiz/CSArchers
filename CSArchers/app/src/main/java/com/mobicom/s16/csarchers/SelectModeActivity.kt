package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivitySelectModeBinding

class SelectModeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivitySelectModeBinding = ActivitySelectModeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.selectModeBtnSim.setOnClickListener({
            val intent = Intent(this, TopicsActivity::class.java)
            startActivity(intent)
        })
    }
}