package com.example.quizapp.model

class MultipleChoiceQuestion(
    override var question: String,
    override var answer: String,
    var choices: ArrayList<String>,
    override var img: String = "") : Question() {
}
