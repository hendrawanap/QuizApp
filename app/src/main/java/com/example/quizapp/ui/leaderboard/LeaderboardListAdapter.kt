package com.example.quizapp.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quizapp.databinding.ItemRowLeaderboardBinding
import com.example.quizapp.model.UserLeaderboard

class LeaderboardListAdapter(private val leaderboardList: List<UserLeaderboard>) : RecyclerView.Adapter<LeaderboardListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemRowLeaderboardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardListAdapter.MyViewHolder {
        val binding = ItemRowLeaderboardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(leaderboardList[position]){
                binding.tvLeaderboard.text = (position+1).toString()
                Glide.with(holder.itemView).load(this.image).into(binding.leaderboardImg)
                binding.tvName.text = this.name
                binding.tvScore.text = this.score.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }


}