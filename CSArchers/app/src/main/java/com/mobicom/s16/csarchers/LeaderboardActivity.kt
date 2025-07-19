package com.mobicom.s16.csarchers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobicom.s16.csarchers.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : ComponentActivity() {

    private var userList: ArrayList<User> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val viewBinding: ActivityLeaderboardBinding =
            ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        recyclerView = viewBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        myAdapter = LeaderboardAdapter(userList)
        recyclerView.adapter = myAdapter


        val mydbHelper = MyDbHelper(this)
        mydbHelper.getUsers { users ->
            userList.clear()
            userList.addAll(users.sortedByDescending { it.weeklyScore }) // sort before showing
            myAdapter.notifyDataSetChanged()
        }
    }
}
