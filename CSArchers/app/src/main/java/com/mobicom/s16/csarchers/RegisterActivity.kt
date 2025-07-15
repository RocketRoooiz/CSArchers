package com.mobicom.s16.csarchers

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mobicom.s16.csarchers.databinding.ActivityRegisterBinding
import org.mindrot.jbcrypt.BCrypt


import com.mobicom.s16.csarchers.databinding.ActivitySelectModeBinding

class RegisterActivity : ComponentActivity() {

    private companion object {
        private const val TAG = "LOG" // Or any descriptive name
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val db = Firebase.firestore


        val counterRef = db.collection("counters").document("accountCounter")
        val usersRef = db.collection("users")

        viewBinding.registerBtnSubmit.setOnClickListener {
            val username = viewBinding.registerEtUsername.text.toString()
            val email = viewBinding.registerEtEmail.text.toString()
            val plainPassword = viewBinding.registerEtPassword.text.toString()

            // 1️⃣ Password validation
            val specialCharRegex = Regex(".*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?].*")
            if (plainPassword.length < 5 || !specialCharRegex.matches(plainPassword)) {
                Toast.makeText(this, "Password must be at least 5 characters and contain a special character.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 2️⃣ Check if username is taken
            db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { usernameDocs ->
                    if (!usernameDocs.isEmpty) {
                        Toast.makeText(this, "Username is already taken.", Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }

                    // 3️⃣ Check if email is taken
                    db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { emailDocs ->
                            if (!emailDocs.isEmpty) {
                                Toast.makeText(this, "Email is already registered.", Toast.LENGTH_LONG).show()
                                return@addOnSuccessListener
                            }

                            // 4️⃣ Proceed with account creation
                            db.runTransaction { transaction ->
                                val snapshot = transaction.get(counterRef)
                                val lastId = snapshot.getLong("lastAccountId") ?: 0
                                val newId = lastId + 1

                                transaction.update(counterRef, "lastAccountId", newId)

                                val hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt())
                                val user = hashMapOf(
                                    "email" to email,
                                    "username" to username,
                                    "password" to hashedPassword
                                )

                                transaction.set(usersRef.document(newId.toString()), user)
                                newId
                            }.addOnSuccessListener { newId ->
                                Log.d(TAG, "Account created with ID: $newId")
                                val intent = Intent(this, SelectModeActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener { e ->
                                Log.w(TAG, "Failed to create account", e)
                                Toast.makeText(this, "Account creation failed: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error checking email: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error checking username: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }











        viewBinding.registerTvLogin.setOnClickListener ({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

    }
}