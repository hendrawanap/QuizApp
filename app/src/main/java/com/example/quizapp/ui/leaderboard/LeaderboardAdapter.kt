package com.example.quizapp.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.SliderItemBinding
import com.example.quizapp.model.Leaderboard

class LeaderboardAdapter(private val leaderboardList: List<Leaderboard>) : RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: SliderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardAdapter.MyViewHolder {
        val binding = SliderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(leaderboardList[position]){
                binding.tvKategori.text = this.topic
                binding.leaderboardImg1.setImageResource(this.user1.image)
                binding.leaderboardImg2.setImageResource(this.user2.image)
                binding.leaderboardImg3.setImageResource(this.user3.image)
                binding.score1.text = this.user1.score.toString()
                binding.score2.text = this.user2.score.toString()
                binding.score3.text = this.user3.score.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }


}