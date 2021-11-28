package com.example.quizapp.model

class Quiz(_questions: ArrayList<Question>) {
    private var scores = HashMap<Question, Int>()
    private var timeElapsed: Long = 0
    private val questions: ArrayList<Question> = _questions
    private var currentIndex: Int = 0

    companion object {
        private const val CORRECT_SCORE: Int = 250
    }

    init {
        for (question in _questions) {
            scores[question] = 0
        }
    }

    fun answer(question: Question, answer: String, timeRemaining: Long, timeTotal: Long): Boolean {
        val isCorrect = question.answer == answer
        if (isCorrect) {
            scores[question] = calculateScore(timeRemaining, timeTotal)
        }
        timeElapsed += timeTotal - timeRemaining
        return isCorrect
    }

    fun calculateScore(timeRemaining: Long, timeTotal: Long): Int {
        return (CORRECT_SCORE * (1 + timeRemaining * 1f / timeTotal) ).toInt()
    }

    fun getScore(): Int {
        var score = 0
        scores.forEach { (_, value) -> score += value }
        return score
    }

    fun getAccuracy(): Float {
        return (scores.filter { (_, value) -> value != 0 }.size * 1f / scores.size)
    }

    fun getTimeElapsed(): Long = timeElapsed

    fun getScores(): HashMap<Question, Int> = scores

    fun nextQuestion(): Question? {
        if (currentIndex < questions.size){
            return questions[currentIndex++]
        } else {
            return null
        }
    }

    fun getAnswer(): String = questions[currentIndex - 1].answer
}
