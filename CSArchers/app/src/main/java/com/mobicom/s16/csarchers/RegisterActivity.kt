package com.mobicom.s16.csarchers

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
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
            val username = viewBinding.registerEtUsername.text.toString().trim()
            val email = viewBinding.registerEtEmail.text.toString().trim()
            val plainPassword = viewBinding.registerEtPassword.text.toString().trim()

            val specialCharRegex = Regex(".*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?].*")
            if (plainPassword.length < 5 || !specialCharRegex.containsMatchIn(plainPassword)) {
                Toast.makeText(this, "Password must be at least 5 characters and contain a special character.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

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
                        startActivity(Intent(this, SelectModeActivity::class.java))
                    }.addOnFailureListener { e ->
                        Log.w(TAG, "Failed to create account", e)
                        Toast.makeText(this, "Account creation failed: ${e.message}", Toast.LENGTH_LONG).show()
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