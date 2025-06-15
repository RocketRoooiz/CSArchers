package com.mobicom.s16.csarchers

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityGameBinding

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityGameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val drawingView = findViewById<DrawingView>(R.id.drawing_view)
    }
}