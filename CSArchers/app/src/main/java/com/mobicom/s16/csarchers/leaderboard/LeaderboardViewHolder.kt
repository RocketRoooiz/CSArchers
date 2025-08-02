package com.mobicom.s16.csarchers.leaderboard

import androidx.recyclerview.widget.RecyclerView
import com.mobicom.s16.csarchers.User
import com.mobicom.s16.csarchers.databinding.UsersLayoutBinding

class LeaderboardViewHolder (private val viewBinding: UsersLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(user: User, rank: Int, isWeekly: Boolean) {
        viewBinding.usernameTv.text = user.username
        viewBinding.rankTv.text = (rank + 1).toString()
        viewBinding.scoreTv.text = if (isWeekly) {
            user.weeklyScore.toString()
        } else {
            user.totalScore.toString()
        }
    }

    
}
