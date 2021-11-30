package com.example.quizapp.ui.leaderboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.databinding.FragmentLeaderboardBinding
import com.example.quizapp.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.quizapp.model.Leaderboard
import com.example.quizapp.model.Record
import com.example.quizapp.model.UserLeaderboard
import kotlinx.android.synthetic.main.fragment_home.*


class LeaderboardFragment : Fragment() {

    companion object {
        fun newInstance() = LeaderboardFragment()
    }

    private lateinit var viewModel: LeaderboardViewModel
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!
    lateinit var ldr: MutableList<UserLeaderboard>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LeaderboardViewModel::class.java)
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        binding.toggleKategori.left.text = "Pilihan Ganda"
        binding.toggleKategori.right.text = "Isian"
        binding.toggleKategori.toggle.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.toggleKategori.left.id -> {
                    when (viewModel.selectedTopic.value) {
                        "Makanan" -> viewModel.setSelectedLeaderboard(0)
                        "Ikon" -> viewModel.setSelectedLeaderboard(2)
                        "Wisata" -> viewModel.setSelectedLeaderboard(4)
                    }
                    viewModel.setSelectedType("Multiple")
                }
                binding.toggleKategori.right.id -> {
                    when (viewModel.selectedTopic.value) {
                        "Makanan" -> viewModel.setSelectedLeaderboard(1)
                        "Ikon" -> viewModel.setSelectedLeaderboard(3)
                        "Wisata" -> viewModel.setSelectedLeaderboard(5)
                    }
                    viewModel.setSelectedType("Short")
                }

            }
        }
        viewModel.selectedTopic.observe(viewLifecycleOwner, Observer { topic ->
            binding.btnRight.setOnClickListener {
                if (topic == "Makanan") {
                    binding.tvTopic.setText("Ikon")
                    viewModel.setSelectedTopic("Ikon")
                    when (viewModel.selectedType.value) {
                        "Multiple" -> viewModel.setSelectedLeaderboard(2)
                        "Short" -> viewModel.setSelectedLeaderboard(3)
                    }
                } else if (topic == "Ikon") {
                    binding.tvTopic.setText("Wisata")
                    viewModel.setSelectedTopic("Wisata")
                    when (viewModel.selectedType.value) {
                        "Multiple" -> viewModel.setSelectedLeaderboard(4)
                        "Short" -> viewModel.setSelectedLeaderboard(5)
                    }
                } else if (topic == "Wisata") {
                    binding.tvTopic.setText("Makanan")
                    viewModel.setSelectedTopic("Makanan")
                    when (viewModel.selectedType.value) {
                        "Multiple" -> viewModel.setSelectedLeaderboard(0)
                        "Short" -> viewModel.setSelectedLeaderboard(1)
                    }
                }
            }

            binding.btnLeft.setOnClickListener {
                if (topic == "Makanan") {
                    binding.tvTopic.setText("Wisata")
                    viewModel.setSelectedTopic("Wisata")
                    when (viewModel.selectedType.value) {
                        "Multiple" -> viewModel.setSelectedLeaderboard(4)
                        "Short" -> viewModel.setSelectedLeaderboard(5)
                    }
                } else if (topic == "Wisata") {
                    binding.tvTopic.setText("Ikon")
                    viewModel.setSelectedTopic("Ikon")
                    when (viewModel.selectedType.value) {
                        "Multiple" -> viewModel.setSelectedLeaderboard(2)
                        "Short" -> viewModel.setSelectedLeaderboard(3)
                    }
                } else if (topic == "Ikon") {
                    binding.tvTopic.setText("Makanan")
                    viewModel.setSelectedTopic("Makanan")
                    when (viewModel.selectedType.value) {
                        "Multiple" -> viewModel.setSelectedLeaderboard(0)
                        "Short" -> viewModel.setSelectedLeaderboard(1)
                    }
                }
            }
//            when(viewModel.selectedTopic.value){
//                "Makanan" -> ldr = viewModel.leaderboardList.value[]
//            }
        })


        ldr = mutableListOf<UserLeaderboard>()
        viewModel.selectedLeaderboard.observe(viewLifecycleOwner, Observer {
            ldr = it.users
            binding.rvLeaderboard.leaderboardList.adapter = LeaderboardListAdapter(ldr)
        })

        return binding.root
    }


//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(LeaderboardViewModel::class.java)
//
//
//    }

}