package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityRegisterBinding
import com.mobicom.s16.csarchers.databinding.ActivitySelectModeBinding

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.registerBtnSubmit.setOnClickListener({
            val intent = Intent(this, SelectModeActivity::class.java)
            startActivity(intent)
        })

        viewBinding.registerTvLogin.setOnClickListener ({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

    }
}