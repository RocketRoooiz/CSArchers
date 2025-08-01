package com.mobicom.s16.csarchers

import android.content.Intent
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mobicom.s16.csarchers.databinding.ActivityMainBinding
import com.mobicom.s16.csarchers.notification_senders.AlarmScheduler


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)






            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)


    }

}

