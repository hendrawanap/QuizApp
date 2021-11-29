package com.example.quizapp.ui.home

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.model.Leaderboard
import com.example.quizapp.model.Record
import com.example.quizapp.ui.leaderboard.LeaderboardAdapter
import com.example.quizapp.ui.profile.ProfileViewModel
import com.example.quizapp.ui.profile.RecentlyPlayedAdapter
import com.example.quizapp.ui.quiz.QuizViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var ldr : MutableList<Leaderboard>
    lateinit var newhistory : MutableList<Record>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.goToLeaderboard.setOnClickListener { findNavController().navigate(R.id.navigation_leaderboard) }

        homeViewModel.user.observe(viewLifecycleOwner, Observer {
            binding.username.text = it.username
            Glide.with(this).load(it.displayImg).into(binding.firebaseImg)

        })


        ldr = mutableListOf<Leaderboard>()
        homeViewModel.leaderboard.observe(viewLifecycleOwner,Observer{
            ldr = it
            binding.viewPager2.adapter = LeaderboardAdapter(ldr)
            binding.indicator.setViewPager(binding.viewPager2)
        })

        newhistory = mutableListOf<Record>()
        profileViewModel.newhistory.observe(viewLifecycleOwner, Observer {
            newhistory = it
            binding.rvHistory.rvProfile.adapter = RecentlyPlayedAdapter(newhistory)
        })

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}