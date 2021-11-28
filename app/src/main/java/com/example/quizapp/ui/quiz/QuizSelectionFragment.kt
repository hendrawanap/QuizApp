package com.example.quizapp.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
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

        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim).setPopExitAnim(R.anim.nav_default_pop_exit_anim).build()

        binding.apply {
            selectionFood.setOnClickListener {
                viewModel.setTopic("Makanan")
                findNavController().navigate(R.id.navigation_quiz_category_selection,null, navOptions)
            }
            selectionIcon.setOnClickListener {
                viewModel.setTopic("Ikon")
                findNavController().navigate(R.id.navigation_quiz_category_selection, null, navOptions)
            }
            selectionTourism.setOnClickListener {
                viewModel.setTopic("Wisata")
                findNavController().navigate(R.id.navigation_quiz_category_selection, null, navOptions)
            }
        }

        return binding.root
    }

}