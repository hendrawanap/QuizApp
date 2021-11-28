package com.example.quizapp.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.databinding.ItemRowHistoryIkonBinding
import com.example.quizapp.model.Record

class RecentlyPlayedAdapter (private val recentlyPlayedList : ArrayList<Record>):
    RecyclerView.Adapter<RecentlyPlayedAdapter.ListViewHolder>(){

//    private var _binding: FragmentProfileBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!

    inner class ListViewHolder(val binding: ItemRowHistoryIkonBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowHistoryIkonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder){
            with(recentlyPlayedList[position]){
                binding.tvItemName.text = this.topic
                binding.score.text = this.score.toString()
                when (this.topic){
                    "Makanan" -> binding.cardIndicator.setBackgroundResource(R.drawable.custom_card_left_makanan)
                    "Ikon" -> binding.cardIndicator.setBackgroundResource(R.drawable.custom_card_left)
                    "Wisata" -> binding.cardIndicator.setBackgroundResource(R.drawable.custom_card_left_wisata)
                }
                when(this.type){
                    "Multiple" -> binding.tvItemDescription.text = "Pilihan Ganda"
                    "Short" -> binding.tvItemDescription.text = "Isian Singkat"
                }
            }
        }
    }

    override fun getItemCount(): Int = recentlyPlayedList.size

}