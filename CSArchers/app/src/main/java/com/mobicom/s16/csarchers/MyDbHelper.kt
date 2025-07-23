
package com.mobicom.s16.csarchers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobicom.s16.csarchers.IntentKeys

import com.mobicom.s16.csarchers.SelectModeActivity

class MyDbHelper(private val context: Context) {
    private companion object {
        private const val TAG = "LOG" // Or any descriptive name
    }
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    fun addUser(email: String, username: String, password: String){


        val usernameTask = db.collection("users").whereEqualTo("username", username).get()
        val emailTask = db.collection("users").whereEqualTo("email", email).get()

        Tasks.whenAllSuccess<QuerySnapshot>(usernameTask, emailTask)
            .addOnSuccessListener { results ->
                val usernameDocs = results[0]
                val emailDocs = results[1]

                if (!usernameDocs.isEmpty) {
                    Toast.makeText(context, "Username is already taken.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                if (!emailDocs.isEmpty) {
                    Toast.makeText(context, "Email is already registered.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                // Create user with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            val uid = firebaseUser?.uid ?: return@addOnCompleteListener

                            val user = hashMapOf(
                                "email" to email,
                                "username" to username,
                                "weeklyScore" to 0,
                                "totalScore" to 0
                                // No need to store password at all
                            )

                            db.collection("users").document(uid).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Failed to store user data", e)
                                    Toast.makeText(context, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                                }

                            val i = Intent(context, SelectModeActivity::class.java)
                            i.putExtra(IntentKeys.USER_NAME_KEY.name, username)
                            context.startActivity(i)
                            if (context is android.app.Activity) {
                                context.finish()
                            }
                            // Store additional user info in Firestore

                        } else {
                            Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error checking credentials: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        // Fetch the username from Firestore using UID
                        db.collection("users").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val username = document.getString("username") ?: "Unknown"
                                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                                    val i = Intent(context, SelectModeActivity::class.java)
                                    i.putExtra(IntentKeys.USER_NAME_KEY.name, username) // Pass the username
                                    context.startActivity(i)
                                    if (context is android.app.Activity) {
                                        context.finish()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "User data not found.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to load user data: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    val message = task.exception?.message ?: "Login failed"
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun logout(){
        auth.signOut()
    }

    fun addScore(score: Int) {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = user.uid
        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentWeekly = snapshot.getLong("weeklyScore") ?: 0
            val currentTotal = snapshot.getLong("totalScore") ?: 0

            val newWeekly = currentWeekly + score
            val newTotal = currentTotal + score

            transaction.update(userRef, mapOf(
                "weeklyScore" to newWeekly,
                "totalScore" to newTotal
            ))
        }.addOnSuccessListener {
            Toast.makeText(context, "Score updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to update score: ${e.message}", e)
            Toast.makeText(context, "Failed to update score: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    fun getUsers(callback: (ArrayList<User>) -> Unit) {
        val userList = ArrayList<User>()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val user = document.toObject(User::class.java)
                    userList.add(user)
                }
                Log.d("Users", "Fetched ${userList.size} users")
                callback(userList) // Return the list via callback
            }
            .addOnFailureListener { exception ->
                Log.w("Users", "Error getting users: ", exception)
                callback(ArrayList()) // Return empty list on failure
            }
    }




}
