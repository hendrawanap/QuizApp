package com.example.quizapp.ui.quiz

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
import com.example.quizapp.databinding.QuizProcessFragmentBinding
import com.example.quizapp.model.MultipleChoiceQuestion
import com.google.firebase.storage.FirebaseStorage

class QuizProcessFragment : Fragment() {

    companion object {
        fun newInstance() = QuizProcessFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
    private var _binding: QuizProcessFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QuizProcessFragmentBinding.inflate(inflater, container, false)

        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.question.text = it.question
                if (it is MultipleChoiceQuestion) {
                    binding.choice1.apply {
                        text = it.choices[0]
                        setOnClickListener {
                            viewModel.answer(text.toString())
                        }
                    }
                    binding.choice2.apply {
                        text = it.choices[1]
                        setOnClickListener {
                            viewModel.answer(text.toString())
                        }
                    }
                    binding.choice3.apply {
                        text = it.choices[2]
                        setOnClickListener {
                            viewModel.answer(text.toString())
                        }
                    }
                    binding.choice4.apply {
                        text = it.choices[3]
                        setOnClickListener {
                            viewModel.answer(text.toString())
                        }
                    }
                }
                if (it.img == "") {
                    binding.questionImg.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.questionImg.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    var storageRef = FirebaseStorage.getInstance().reference
                    storageRef.child(it.img).downloadUrl.addOnSuccessListener {
                        binding.progressBar.visibility = View.GONE
                        binding.questionImg.visibility = View.VISIBLE
                        Glide.with(this).load(it).into(binding.questionImg)
                    }
                }
            }
        })

        viewModel.showCorrect.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.isCorrect.visibility = View.VISIBLE
            } else {
                binding.isCorrect.visibility = View.INVISIBLE
            }
        })

        viewModel.isCorrect.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.isCorrect.text = "Benar"
            } else {
                binding.isCorrect.text = "Salah"
            }
        })

        viewModel.isFinished.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.navigation_quiz_result)
            }
        })

        viewModel.timeRemaining.observe(viewLifecycleOwner, Observer {
            binding.timer.text = it.toString()
        })

        viewModel.score.observe(viewLifecycleOwner, Observer {
            binding.score.text = it.toString()
        })

        return binding.root
    }

}