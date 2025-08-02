package com.mobicom.s16.csarchers.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobicom.s16.csarchers.User
import com.mobicom.s16.csarchers.databinding.UsersLayoutBinding

class LeaderboardAdapter (private val data: ArrayList<User>, private var isWeekly: Boolean): RecyclerView.Adapter<LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val itemViewBinding: UsersLayoutBinding = UsersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindData(data[position], position, isWeekly)
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }

    fun updateMode(isWeekly: Boolean) {
        this.isWeekly = isWeekly
        notifyDataSetChanged()
    }
}
