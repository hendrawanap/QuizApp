package com.example.quizapp.model

data class Highestscore(
    val category: String = "",
    val recordId: String = "",
    val score: Int = 0,
    val topic: String = "",
    val userId: String = "",
)
