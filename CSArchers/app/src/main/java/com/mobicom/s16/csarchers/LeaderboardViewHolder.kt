package com.mobicom.s16.csarchers

import androidx.recyclerview.widget.RecyclerView
import com.mobicom.s16.csarchers.databinding.UsersLayoutBinding

class LeaderboardViewHolder (private val viewBinding: UsersLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(user: User, rank: Int) {
        viewBinding.scoreTv.text = user.weeklyScore.toString()
        viewBinding.usernameTv.text = user.username
        viewBinding.rankTv.text = (rank + 1).toString()
    }

    
}
