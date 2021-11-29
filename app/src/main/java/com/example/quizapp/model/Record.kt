package com.example.quizapp.model

import java.util.*

data class Record(
    val accuracy: Float = 0.0f,
    val duration: Long = 0,
    val finishedAt: Date = Date(),
    val score: Int = 0,
    val topic: String = "",
    val type: String = "",
    val userId: String = ""
)
