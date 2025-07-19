package com.mobicom.s16.csarchers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobicom.s16.csarchers.databinding.UsersLayoutBinding

class LeaderboardAdapter (private val data: ArrayList<User>): RecyclerView.Adapter<LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {


        val itemViewBinding: UsersLayoutBinding = UsersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(itemViewBinding)
    }



    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindData(this.data[position], position)
    }


    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }
}
