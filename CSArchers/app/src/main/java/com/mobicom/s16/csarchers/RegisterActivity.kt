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

    private companion object {
        private const val TAG = "LOG" // Or any descriptive name
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val db = Firebase.firestore
        auth = Firebase.auth



        viewBinding.registerBtnSubmit.setOnClickListener {
            val username = viewBinding.registerEtUsername.text.toString().trim()
            val email = viewBinding.registerEtEmail.text.toString().trim()
            val plainPassword = viewBinding.registerEtPassword.text.toString().trim()

            val specialCharRegex = Regex(".*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?].*")
            if (plainPassword.length < 5 || !specialCharRegex.containsMatchIn(plainPassword)) {
                Toast.makeText(this, "Password must be at least 5 characters and contain a special character.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Check if username or email already exists in Firestore
            val usernameTask = db.collection("users").whereEqualTo("username", username).get()
            val emailTask = db.collection("users").whereEqualTo("email", email).get()

            Tasks.whenAllSuccess<QuerySnapshot>(usernameTask, emailTask)
                .addOnSuccessListener { results ->
                    val usernameDocs = results[0]
                    val emailDocs = results[1]

                    if (!usernameDocs.isEmpty) {
                        Toast.makeText(this, "Username is already taken.", Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }

                    if (!emailDocs.isEmpty) {
                        Toast.makeText(this, "Email is already registered.", Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }

                    // Create user with Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, plainPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = auth.currentUser
                                val uid = firebaseUser?.uid ?: return@addOnCompleteListener

                                // Store additional user info in Firestore
                                val user = hashMapOf(
                                    "email" to email,
                                    "username" to username
                                    // No need to store password at all
                                )

                                db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                        val i = Intent(this, SelectModeActivity::class.java)
                                        i.putExtra(IntentKeys.USER_NAME_KEY.name, username)
                                        startActivity(i)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Failed to store user data", e)
                                        Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error checking credentials: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }


        viewBinding.registerTvLogin.setOnClickListener ({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

    }


}