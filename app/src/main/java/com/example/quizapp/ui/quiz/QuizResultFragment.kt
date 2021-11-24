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
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizResultBinding

class QuizResultFragment : Fragment() {

    companion object {
        fun newInstance() = QuizResultFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
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
                binding.accuracy.text = it.toString()
            })
            timeElapsed.observe(viewLifecycleOwner, Observer {
                binding.timeElapsed.text = it.toString()
            })
        }

        binding.backToHomeBtn.setOnClickListener { findNavController().navigate(R.id.navigation_home) }

        return binding.root
    }

}