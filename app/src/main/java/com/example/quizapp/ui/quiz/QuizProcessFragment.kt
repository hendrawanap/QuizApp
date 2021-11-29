package com.example.quizapp.ui.quiz

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizProcessBinding
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.ShortAnswerQuestion
import com.google.firebase.storage.FirebaseStorage

class QuizProcessFragment : Fragment() {

    companion object {
        fun newInstance() = QuizProcessFragment()
    }

    private val viewModel: QuizViewModel by activityViewModels()
    private var _binding: FragmentQuizProcessBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizProcessBinding.inflate(inflater, container, false)

        binding.apply {
            hintBtn.visibility = View.GONE
            hintBtn.setOnClickListener {
                viewModel.showHint()
                it.visibility = View.GONE
            }
        }

        binding.viewMultipleChoice.apply {
            root.visibility = View.GONE
            choice1.setOnClickListener {
                viewModel.answer(choice1.text.toString())
                if (!choice1.text.toString().equals(viewModel.currentAnswer.value, true))
                    changeStyle("incorrect", choice1)
            }
            choice2.setOnClickListener {
                viewModel.answer(choice2.text.toString())
                if (!choice2.text.toString().equals(viewModel.currentAnswer.value, true))
                    changeStyle("incorrect", choice2)
            }
            choice3.setOnClickListener {
                viewModel.answer(choice3.text.toString())
                if (!choice3.text.toString().equals(viewModel.currentAnswer.value, true))
                    changeStyle("incorrect", choice3)
            }
            choice4.setOnClickListener {
                viewModel.answer(choice4.text.toString())
                if (!choice4.text.toString().equals(viewModel.currentAnswer.value, true))
                    changeStyle("incorrect", choice4)
            }
        }

        binding.viewShortAnswer.apply {
            root.visibility = View.GONE
            answerBtn.setOnClickListener {
                viewModel.answer(answerField.text.toString().trim())
                if (!viewModel.currentAnswer.value.equals(answerField.text.toString().trim(), true)) {
                    changeStyle("incorrect", answerField)
                }
                viewModel.showHint()
                binding.hintText.text = viewModel.currentAnswer.value
                hideKeyboard()
            }
            answerField.filters = arrayOf(InputFilter.AllCaps())
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.question.text = it.question

                if (it is MultipleChoiceQuestion) {
                    binding.hintBtn.visibility = View.GONE
                    binding.viewShortAnswer.root.visibility = View.GONE
                    binding.viewMultipleChoice.root.visibility = View.VISIBLE

                    binding.viewMultipleChoice.apply {
                        choice1.text = it.choices[0]
                        choice2.text = it.choices[1]
                        choice3.text = it.choices[2]
                        choice4.text = it.choices[3]
                    }

                } else if (it is ShortAnswerQuestion) {
                    binding.viewMultipleChoice.root.visibility = View.GONE
                    binding.hintBtn.visibility = View.VISIBLE
                    binding.viewShortAnswer.root.visibility = View.VISIBLE
                    binding.viewShortAnswer.answerField.setText("")

                    binding.hintText.text = it.hint
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

        viewModel.showHint.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.hintText.visibility = View.VISIBLE
            } else {
                binding.hintText.visibility = View.GONE
            }
        })

        viewModel.showCorrect.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.viewMultipleChoice.apply {
                    when (viewModel.currentAnswer.value) {
                        choice1.text.toString() -> changeStyle("correct", choice1)
                        choice2.text.toString() -> changeStyle("correct", choice2)
                        choice3.text.toString() -> changeStyle("correct", choice3)
                        choice4.text.toString() -> changeStyle("correct", choice4)
                    }
                }

                binding.viewShortAnswer.apply {
                    when (viewModel.currentAnswer.value) {
                        answerField.text.toString().trim() -> changeStyle("correct", answerField)
                    }
                }
            } else {
                binding.viewMultipleChoice.apply {
                    changeStyle(view = choice1)
                    changeStyle(view = choice2)
                    changeStyle(view = choice3)
                    changeStyle(view = choice4)
                }

                binding.viewShortAnswer.apply {
                    changeStyle(view = answerField)
                }
            }
        })

//        viewModel.isCorrect.observe(viewLifecycleOwner, Observer {
//            if (it) {
//                binding.isCorrect.text = "Benar"
//            } else {
//                binding.isCorrect.text = "Salah"
//            }
//        })

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

        viewModel.questionNumber.observe(viewLifecycleOwner, Observer {
            val text = "SOAL $it"
            binding.questionNumber.text = text
        })

        return binding.root
    }

    private fun changeStyle(style: String = "default", view: TextView) {
        var bg: Int = R.drawable.choice_default;
        var textColor: Int = R.color.black;

        when(style) {
            "correct" -> {
                bg = R.drawable.choice_correct
                textColor = R.color.white
            }
            "incorrect" -> {
                bg = R.drawable.choice_incorrect
                textColor = R.color.white
            }
        }
        view.setBackgroundResource(bg)
        view.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}