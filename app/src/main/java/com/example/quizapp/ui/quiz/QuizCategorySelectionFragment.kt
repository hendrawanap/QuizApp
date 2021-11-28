package com.example.quizapp.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizCategorySelectionBinding
import com.example.quizapp.databinding.FragmentQuizSelectionBinding

class QuizCategorySelectionFragment : Fragment() {

    companion object {
        fun newInstance() = QuizCategorySelectionFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
    private var _binding: FragmentQuizCategorySelectionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizCategorySelectionBinding.inflate(inflater, container, false)
        
        binding.apply {
            selectionMultipleChoice.setOnClickListener {
                viewModel.generateMultipleChoiceQuiz()
                findNavController().navigate(R.id.navigation_quiz_start)
            }
            selectionShortAnswer.setOnClickListener {
                viewModel.generateShortAnswerQuiz()
                findNavController().navigate(R.id.navigation_quiz_start)
            }
        }

        return binding.root
    }

}