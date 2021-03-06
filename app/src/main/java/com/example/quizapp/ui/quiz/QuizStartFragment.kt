package com.example.quizapp.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizStartBinding

class QuizStartFragment : Fragment() {

    companion object {
        fun newInstance() = QuizStartFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
    private var _binding: FragmentQuizStartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuizStartBinding.inflate(inflater, container, false)

        binding.countDown.visibility = View.GONE

        viewModel.isLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.startQuiz.visibility = View.VISIBLE
                binding.textStart.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.startQuiz.visibility = View.GONE
                binding.textStart.visibility = View.GONE
            }
        })

        viewModel.count.observe(viewLifecycleOwner, Observer {
            binding.countDown.text = it
        })

        viewModel.isStarted.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.navigation_quiz_process)
            }
        })

        viewModel.topic.observe(viewLifecycleOwner, Observer {
            binding.topicTitle.text = it
            when (it) {
                "Makanan" -> binding.topicDesc.setText(R.string.start_makanan_desc)
                "Wisata" -> binding.topicDesc.setText(R.string.start_wisata_desc)
                "Ikon" -> binding.topicDesc.setText(R.string.start_ikon_desc)
            }
        })

        binding.startQuiz.apply {
            setOnClickListener {
                viewModel.startQuizCountDown()
                visibility = View.GONE
                binding.textStart.visibility = View.GONE
                binding.countDown.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

}