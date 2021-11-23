package com.example.quizapp.ui.quiz

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.Question
import com.example.quizapp.model.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizViewModel : ViewModel() {
    private lateinit var timer: CountDownTimer
    private val _count = MutableLiveData<String>()
    private val _isStarted = MutableLiveData<Boolean>()
    private val _isFinished = MutableLiveData<Boolean>()
    private val _quiz = MutableLiveData<Quiz>()
    private val _currentQuestion = MutableLiveData<Question>()
    private val _isCorrect = MutableLiveData<Boolean>()
    private val _showCorrect = MutableLiveData<Boolean>()
    private val _timeRemaining = MutableLiveData<Int>()
    private val _score = MutableLiveData<Int>()
    private val _timeElapsed = MutableLiveData<Int>()
    private val _accuracy = MutableLiveData<Float>()
    private val _topic = MutableLiveData<String>()
    private val _isLoaded = MutableLiveData<Boolean>()

    val count: LiveData<String> = _count
    val isStarted: LiveData<Boolean> = _isStarted
    val isFinished: LiveData<Boolean> = _isFinished
    val currentQuestion: LiveData<Question> = _currentQuestion
    val isCorrect: LiveData<Boolean> = _isCorrect
    val showCorrect: LiveData<Boolean> = _showCorrect
    val timeRemaining: LiveData<Int> = _timeRemaining
    val score: LiveData<Int> = _score
    val timeElapsed: LiveData<Int> = _timeElapsed
    val accuracy: LiveData<Float> = _accuracy
    val isLoaded: LiveData<Boolean> = _isLoaded

    companion object {
        private const val DURATION: Long = 30000
    }

    fun generateMultipleChoiceQuiz(topic: String, length: Int = 10) {
        _isLoaded.value = false
        _topic.value = topic
        val db = Firebase.firestore
        val collectionRef = db.collection("questions")
        val questions = ArrayList<Question>()
        collectionRef.whereEqualTo("topic", topic).get().addOnSuccessListener { result ->
            for (document in result) {
                var choices = document.get("choices") as ArrayList<String>
                choices.shuffle()
                if (document.get("img") != null) {
                    questions.add(MultipleChoiceQuestion(
                        question = document.get("question").toString(),
                        answer = document.get("answer").toString(),
                        choices = choices,
                        img = document.get("img").toString()
                    ))
                } else {
                    questions.add(MultipleChoiceQuestion(
                        question = document.get("question").toString(),
                        answer = document.get("answer").toString(),
                        choices = choices
                    ))
                }
            }
            questions.shuffle()
            if (length <= questions.size) {
                _quiz.value = Quiz(questions.slice(IntRange(0, length-1)) as ArrayList<Question>)
            } else {
                _quiz.value = Quiz(questions)
            }
            _isLoaded.value = true
        }
    }

    fun startQuizCountDown() {
        _count.value = "3";
        _isStarted.value = false;
        timer = object : CountDownTimer(3500, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 == 2L) {
                    _count.value = "2"
                } else if (millisUntilFinished / 1000 == 1L) {
                    _count.value = "1"
                } else if (millisUntilFinished / 1000 == 0L) {
                    _count.value = "Mulai"
                }
            }

            override fun onFinish() {
                startQuiz()
            }
        }.start()
    }

    fun startQuiz() {
        _isStarted.value = true
        _isFinished.value = false
        _score.value = 0
        nextQuestion()
    }

    fun answer(answer: String) {
        _isCorrect.value = _quiz.value!!.answer(_currentQuestion.value!!, answer, _timeRemaining.value!!.times(1000L), DURATION)
        _score.value = _quiz.value!!.getScore()
        _showCorrect.value = true
        timer.cancel()
        timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                nextQuestion()
            }
        }.start()
    }

    fun nextQuestion() {
        _showCorrect.value = false
        _currentQuestion.value = _quiz.value!!.nextQuestion()
        if (_currentQuestion.value == null) {
            finishQuiz()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _timeRemaining.value = (Companion.DURATION / 1000).toInt()
        timer = object : CountDownTimer(Companion.DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                answer("")
            }
        }
        timer.start()
    }

    private fun finishQuiz() {
        _isFinished.value = true
        _isStarted.value = false
        _accuracy.value = _quiz.value!!.getAccuracy()
        _timeElapsed.value = (_quiz.value!!.getTimeElapsed() / 1000).toInt()
        addToRecords()
    }

    private fun addToRecords() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val recordsRef = db.collection("records")
        recordsRef.add(hashMapOf(
            "duration" to timeElapsed.value,
            "finishedAt" to FieldValue.serverTimestamp(),
            "topic" to _topic.value,
            "type" to "Multiple",
            "score" to score.value,
            "accuracy" to accuracy.value,
            "userId" to user!!.uid,
        ))
    }

}
