package com.example.quizapp.model

data class ShortAnswerQuestion(
    override var question: String,
    override var answer: String,
    var hint: String,
    override var img: String = "") : Question()
