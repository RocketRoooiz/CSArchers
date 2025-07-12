package com.mobicom.s16.csarchers

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityDbSimBinding

class SimulationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityDbSimBinding = ActivityDbSimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }

}