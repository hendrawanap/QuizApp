package com.example.quizapp.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizSelectionBinding

class QuizSelectionFragment : Fragment() {

    companion object {
        fun newInstance() = QuizSelectionFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
    private var _binding: FragmentQuizSelectionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizSelectionBinding.inflate(inflater, container, false)

        binding.apply {
            selectionFood.setOnClickListener {
                viewModel.generateMultipleChoiceQuiz("Makanan")
                findNavController().navigate(R.id.navigation_quiz_start)
            }
            selectionIcon.setOnClickListener {
                viewModel.generateMultipleChoiceQuiz("Ikon")
                findNavController().navigate(R.id.navigation_quiz_start)
            }
            selectionFood.setOnClickListener {
                viewModel.generateMultipleChoiceQuiz("Wisata")
                findNavController().navigate(R.id.navigation_quiz_start)
            }
        }

        return binding.root
    }

}