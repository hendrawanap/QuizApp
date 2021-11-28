package com.example.quizapp.ui.quiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.Question
import com.example.quizapp.model.Quiz
import com.example.quizapp.model.ShortAnswerQuestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
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
    private val _timeRemainingMillis = MutableLiveData<Int>()
    private val _score = MutableLiveData<Int>()
    private val _timeElapsed = MutableLiveData<Int>()
    private val _accuracy = MutableLiveData<Float>()
    private val _topic = MutableLiveData<String>()
    private val _isLoaded = MutableLiveData<Boolean>()
    private val _showHint = MutableLiveData<Boolean>()
    private val _questionNumber = MutableLiveData<Int>()
    private val _category = MutableLiveData<String>()
    private val _isNewHighestScore = MutableLiveData<Boolean>()
    private val _currentAnswer = MutableLiveData<String>()
    private val _highestScore = MutableLiveData<Int>()

    val count: LiveData<String> = _count
    val isStarted: LiveData<Boolean> = _isStarted
    val isFinished: LiveData<Boolean> = _isFinished
    val currentQuestion: LiveData<Question> = _currentQuestion
    val isCorrect: LiveData<Boolean> = _isCorrect
    val showCorrect: LiveData<Boolean> = _showCorrect
    val timeRemaining: LiveData<Int> = _timeRemaining
    val timeRemainingMillis: LiveData<Int> = _timeRemainingMillis
    val score: LiveData<Int> = _score
    val timeElapsed: LiveData<Int> = _timeElapsed
    val accuracy: LiveData<Float> = _accuracy
    val isLoaded: LiveData<Boolean> = _isLoaded
    val showHint: LiveData<Boolean> = _showHint
    val questionNumber: LiveData<Int> = _questionNumber
    val isNewHighestScore: LiveData<Boolean> = _isNewHighestScore
    val currentAnswer: LiveData<String> = _currentAnswer
    val topic: LiveData<String> = _topic
    val category: LiveData<String> = _category
    val highestScore: LiveData<Int> = _highestScore

    companion object {
        private const val DURATION: Long = 30000
    }

    fun generateMultipleChoiceQuiz(length: Int = 10) {
        _category.value = "Multiple"
        _isLoaded.value = false
        val db = Firebase.firestore
        val collectionRef = db.collection("questions")
        val questions = ArrayList<Question>()
        collectionRef.whereEqualTo("topic", _topic.value).get().addOnSuccessListener { result ->
            for (document in result) {
                var choices = (document.get("choices") as ArrayList<String>)
                choices.shuffle()
                if (document.get("img") != null) {
                    questions.add(MultipleChoiceQuestion(
                        question = document.get("question").toString(),
                        answer = document.get("answer").toString().uppercase(),
                        choices = choices.map { r -> r.uppercase() } as ArrayList<String>,
                        img = document.get("img").toString()
                    ))
                } else {
                    questions.add(MultipleChoiceQuestion(
                        question = document.get("question").toString(),
                        answer = document.get("answer").toString().uppercase(),
                        choices = choices.map { r -> r.uppercase() } as ArrayList<String>
                    ))
                }
            }
            questions.shuffle()
            if (questions.size > length) {
                _quiz.value = Quiz(questions.slice(IntRange(0, length-1)) as ArrayList<Question>)
            } else {
                _quiz.value = Quiz(questions)
            }
            _isLoaded.value = true
        }
    }

    fun generateShortAnswerQuiz(length: Int = 10) {
        _category.value = "Short"
        _isLoaded.value = false
        val db = Firebase.firestore
        val collectionRef = db.collection("questions")
        val questions = ArrayList<Question>()
        collectionRef.whereEqualTo("topic", _topic.value).get().addOnSuccessListener { result ->
            for (document in result) {
                var hint = generateHints(document.get("answer").toString().uppercase())
                if (document.get("img") != null) {
                    questions.add(ShortAnswerQuestion(
                        question = document.get("question").toString(),
                        answer = document.get("answer").toString().uppercase(),
                        img = document.get("img").toString(),
                        hint = hint
                    ))
                } else {
                    questions.add(ShortAnswerQuestion(
                        question = document.get("question").toString(),
                        answer = document.get("answer").toString().uppercase(),
                        hint = hint
                    ))
                }
            }
            questions.shuffle()
            if (questions.size > length) {
                _quiz.value = Quiz(questions.slice(IntRange(0, length-1)) as ArrayList<Question>)
            } else {
                _quiz.value = Quiz(questions)
            }
            _isLoaded.value = true
        }
    }

    private fun generateHints(answer: String): String {
        val hints = answer.split(" ").toList().toMutableList()
        for ((index, value) in hints.withIndex()) {
            hints[index] = generateHint(value)
        }
        return hints.joinToString("  ")
    }

    private fun generateHint(answer: String): String {
        val maxHint = 5
        var hint = answer.toCharArray()
        var count = 1
        val indexes = arrayListOf<Int>()
        if (hint.size > 2) {
            count = hint.size / 3 + 1
            if (count > hint.size / 2)
                count--
            if (count > maxHint)
                count = 5
            while (indexes.size < count) {
                val random = hint.indices.random()
                if (!indexes.contains(random))
                    indexes.add(random)
            }
            for (index in hint.indices) {
                if (!indexes.contains(index)) hint[index] = '_'
            }
        } else {
            hint = CharArray(hint.size) { _ -> '_'  }
        }
        return hint.joinToString(" ")
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
        _questionNumber.value = 0
        _isNewHighestScore.value = false
        _highestScore.value = 0
        nextQuestion()
    }

    fun answer(answer: String) {
        _isCorrect.value = _quiz.value!!.answer(_currentQuestion.value!!, answer, _timeRemainingMillis.value!!.toLong(), DURATION)
        _score.value = _quiz.value!!.getScore()
        _currentAnswer.value = _quiz.value!!.getAnswer().uppercase()
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
        _showHint.value = false
        _showCorrect.value = false
        _currentQuestion.value = _quiz.value!!.nextQuestion()
        if (_currentQuestion.value == null) {
            finishQuiz()
        } else {
            _questionNumber.value = _questionNumber.value!!.inc()
            startTimer()
        }
    }

    fun setTopic(topic: String) {
        _topic.value = topic
    }

    fun showHint() {
        _showHint.value = true
    }

    private fun startTimer() {
        _timeRemaining.value = (DURATION / 1000).toInt()
        timer = object : CountDownTimer(DURATION, 100) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.value = (millisUntilFinished / 1000).toInt()
                _timeRemainingMillis.value = millisUntilFinished.toInt()
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
        _timeElapsed.value = _quiz.value!!.getTimeElapsed().toInt()
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
        )).addOnSuccessListener {
            addToHighestScores(it.id)
        }
    }

    private fun addToHighestScores(recordId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val highestScoresRef = db.collection("highestscores")
        highestScoresRef.whereEqualTo("userId", user!!.uid)
            .whereEqualTo("category", _category.value)
            .whereEqualTo("topic", _topic.value)
            .get()
            .addOnCompleteListener { it ->
                if (it.result!!.isEmpty) {
                    highestScoresRef.add(hashMapOf(
                        "category" to _category.value,
                        "score" to _score.value,
                        "topic" to _topic.value,
                        "userId" to user.uid,
                        "recordId" to recordId,
                    )).addOnCompleteListener { setHighestScore() }
                } else {
                    val highestScore = it.result!!.documents[0]
                    if ((highestScore.data!!["score"].toString().toInt()) < _score.value!!) {
                        highestScoresRef.document(highestScore.id).set(hashMapOf(
                            "score" to _score.value,
                            "recordId" to recordId,
                        ), SetOptions.merge()).addOnCompleteListener { setHighestScore() }
                        _isNewHighestScore.value = true
                    } else {
                        setHighestScore()
                    }
                }
            }
    }

    private fun setHighestScore() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val highestScoresRef = db.collection("highestscores")
        highestScoresRef.whereEqualTo("userId", user!!.uid)
            .whereEqualTo("category", _category.value)
            .whereEqualTo("topic", _topic.value)
            .get()
            .addOnCompleteListener { it ->
                val result = it.result!!.documents[0].data!!["score"].toString().toInt()
                Log.d("High Score", result.toString())
                _highestScore.value = result
            }
    }

}
