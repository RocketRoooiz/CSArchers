package com.mobicom.s16.csarchers

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.mobicom.s16.csarchers.databinding.ActivityRegisterBinding
import org.mindrot.jbcrypt.BCrypt


import com.mobicom.s16.csarchers.databinding.ActivitySelectModeBinding

class RegisterActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        val mydbHelper = MyDbHelper(this)

        viewBinding.registerBtnSubmit.setOnClickListener {
            val username = viewBinding.registerEtUsername.text.toString().trim()
            val email = viewBinding.registerEtEmail.text.toString().trim()
            val plainPassword = viewBinding.registerEtPassword.text.toString().trim()

            val specialCharRegex = Regex(".*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?].*")
            if (plainPassword.length < 5 || !specialCharRegex.containsMatchIn(plainPassword)) {
                Toast.makeText(this, "Password must be at least 5 characters and contain a special character.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            mydbHelper.addUser(email, username, plainPassword)

            // Check if username or email already exists in Firestore
        }


        viewBinding.registerTvLogin.setOnClickListener ({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

    }


}