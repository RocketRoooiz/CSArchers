package com.mobicom.s16.csarchers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mobicom.s16.csarchers.databinding.ActivityLoginBinding
import com.mobicom.s16.csarchers.notification_senders.AlarmScheduler
import com.mobicom.s16.csarchers.notification_senders.NotificationHelper


class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        auth = Firebase.auth
        val mydbHelper = MyDbHelper(this)



        viewBinding.loginBtnSubmit.setOnClickListener {
            val email = viewBinding.loginEtEmail.text.toString().trim()
            val password = viewBinding.loginEtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mydbHelper.loginUser(email, password)

        }
        viewBinding.loginTvCreate.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val db = Firebase.firestore
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: "Unknown"
                        val intent = Intent(this, SelectModeActivity::class.java)
                        intent.putExtra(IntentKeys.USER_NAME_KEY.name, username)
                        Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                }
        }
    }
}
