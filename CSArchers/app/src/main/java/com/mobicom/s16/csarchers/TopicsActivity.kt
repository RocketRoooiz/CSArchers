package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivitySelectModeBinding
import com.mobicom.s16.csarchers.databinding.ActivityTopicsBinding

class TopicsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityTopicsBinding = ActivityTopicsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }
}