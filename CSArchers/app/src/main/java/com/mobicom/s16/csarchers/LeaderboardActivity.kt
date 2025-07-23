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
    private var isWeekly = true
    private val WEEKLY_MODE = "Weekly"
    private val ALL_TIME_MODE = "All-time"
    private val WEEKLY_HEADER = "Weekly Leaderboard"
    private val ALL_TIME_HEADER = "All-time Leaderboard"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding: ActivityLeaderboardBinding =
            ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        recyclerView = viewBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        myAdapter = LeaderboardAdapter(userList)
        recyclerView.adapter = myAdapter

        viewBinding.leaderboardTvMode.text = ALL_TIME_MODE
        viewBinding.leaderboardTvHeader.text = WEEKLY_HEADER

        val mydbHelper = MyDbHelper(this)

        // Initial load
        mydbHelper.getUsers { users ->
            updateUserList(users, viewBinding)
        }

        viewBinding.leaderboardBtnSwitch.setOnClickListener {
            isWeekly = !isWeekly
            viewBinding.leaderboardTvMode.text = if (isWeekly) ALL_TIME_MODE else WEEKLY_MODE
            viewBinding.leaderboardTvHeader.text = if (isWeekly) WEEKLY_HEADER else ALL_TIME_HEADER

            // Reload sorted list when mode switches
            mydbHelper.getUsers { users ->
                updateUserList(users, viewBinding)
            }
        }
    }

    private fun updateUserList(users: List<User>, viewBinding: ActivityLeaderboardBinding) {
        userList.clear()

        val sortedUsers = if (isWeekly) {
            users.sortedByDescending { it.weeklyScore }
        } else {
            users.sortedByDescending { it.totalScore }  // Make sure this exists
        }

        userList.addAll(sortedUsers)
        myAdapter.notifyDataSetChanged()

        Log.d("LeaderboardActivity", "User list updated. Count: ${userList.size}, Mode: ${if (isWeekly) "Weekly" else "All-time"}")
    }
}
