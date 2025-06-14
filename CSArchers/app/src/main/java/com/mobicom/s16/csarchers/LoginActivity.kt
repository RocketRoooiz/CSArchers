package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityLoginBinding


class LoginActivity : ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.loginBtnSubmit.setOnClickListener({
            val intent = Intent(this, SelectModeActivity::class.java)
            startActivity(intent)
        })

        viewBinding.loginTvCreate.setOnClickListener ({
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
    }

}