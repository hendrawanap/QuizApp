package com.example.quizapp.ui.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quizapp.databinding.SliderItemBinding
import com.example.quizapp.model.Leaderboard

class LeaderboardAdapter(private val leaderboardList: List<Leaderboard>) :
    RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: SliderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardAdapter.MyViewHolder {
        val binding = SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(leaderboardList[position]) {
                binding.tvKategori.text = "${this.topic} - ${this.type}"
                val noProfile = "https://icon-library.com/images/no-user-image-icon/no-user-image-icon-27.jpg"

                Glide.with(holder.itemView).load(noProfile).into(binding.leaderboardImg1)
                Glide.with(holder.itemView).load(noProfile).into(binding.leaderboardImg2)
                Glide.with(holder.itemView).load(noProfile).into(binding.leaderboardImg3)

                binding.score1.text = "0"
                binding.score2.text = "0"
                binding.score3.text = "0"

                if (this.users.size == 1) {
                    Glide.with(holder.itemView).load(this.users[0].image)
                        .into(binding.leaderboardImg1)
                    binding.score1.text = this.users[0].score.toString()
                } else if (this.users.size == 2) {
                    Glide.with(holder.itemView).load(this.users[0].image)
                        .into(binding.leaderboardImg1)
                    binding.score1.text = this.users[0].score.toString()
                    Glide.with(holder.itemView).load(this.users[1].image)
                        .into(binding.leaderboardImg2)
                    binding.score2.text = this.users[1].score.toString()
                } else if (this.users.size >= 3) {
                    Glide.with(holder.itemView).load(this.users[0].image)
                        .into(binding.leaderboardImg1)
                    binding.score1.text = this.users[0].score.toString()
                    Glide.with(holder.itemView).load(this.users[1].image)
                        .into(binding.leaderboardImg2)
                    binding.score2.text = this.users[1].score.toString()
                    Glide.with(holder.itemView).load(this.users[2].image)
                        .into(binding.leaderboardImg3)
                    binding.score3.text = this.users[2].score.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }


}