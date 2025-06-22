package com.mobicom.s16.csarchers

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityBinarySimBinding

class SimulationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityBinarySimBinding = ActivityBinarySimBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}