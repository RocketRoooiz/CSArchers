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


class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val db = Firebase.firestore

        auth = Firebase.auth

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
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            // Fetch the username from Firestore using UID
                            db.collection("users").document(uid).get()
                                .addOnSuccessListener { document ->
                                    if (document != null && document.exists()) {
                                        username = document.getString("username") ?: "Unknown"
                                        Toast.makeText(
                                            this,
                                            "Login successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val intent = Intent(this, SelectModeActivity::class.java)
                                        intent.putExtra(
                                            IntentKeys.USER_NAME_KEY.name,
                                            username
                                        ) // Pass the username
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "User data not found.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Failed to load user data: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
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
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
