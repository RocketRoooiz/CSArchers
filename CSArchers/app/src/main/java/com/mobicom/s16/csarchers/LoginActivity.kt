package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobicom.s16.csarchers.databinding.ActivityLoginBinding


class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        auth = FirebaseAuth.getInstance()

        viewBinding.loginBtnSubmit.setOnClickListener {
            val email = viewBinding.loginEtEmail.text.toString().trim()
            val password = viewBinding.loginEtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SelectModeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val message = task.exception?.message ?: "Login failed"
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
        }

        viewBinding.loginTvCreate.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
