package com.example.quizapp.ui.quiz

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizResultBinding
import com.example.quizapp.ui.leaderboard.LeaderboardViewModel
import com.example.quizapp.ui.profile.ProfileViewModel

class QuizResultFragment : Fragment() {

    companion object {
        fun newInstance() = QuizResultFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
    private val profileVM: ProfileViewModel by activityViewModels()
    private val leaderboardVM: LeaderboardViewModel by activityViewModels()
    private var _binding: FragmentQuizResultBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizResultBinding.inflate(inflater, container, false)

        viewModel.apply {
            score.observe(viewLifecycleOwner, Observer {
                binding.score.text = it.toString()
            })

            accuracy.observe(viewLifecycleOwner, Observer {
                val format = "${it.times(100).toInt()} %"
                binding.accuracy.text = format
            })

            timeElapsed.observe(viewLifecycleOwner, Observer {
                val minute = it / 60 / 1000
                val second = it / 1000 % 60
                val millis = it % 1000 / 10
                val format = "$minute:$second.$millis"
                binding.timeElapsed.text = format
            })

            topic.observe(viewLifecycleOwner, Observer {
                binding.topic.text = it
            })

            category.observe(viewLifecycleOwner, Observer {
                when (it) {
                    "Multiple" -> binding.category.setText(R.string.category_multiple)
                    "Short" -> binding.category.setText(R.string.category_short)
                }
            })

            highestScore.observe(viewLifecycleOwner, Observer {
                binding.highestScore.text = it.toString()
            })
        }

        profileVM.user.observe(viewLifecycleOwner, Observer {
            binding.username.text = it.username
            Glide.with(this).load(it.displayImg).into(binding.imgresult)
        })

        binding.backToHomeBtn.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
            leaderboardVM.getLeaderboards()
            profileVM.getRecords()
        }

        return binding.root
    }

}