package com.example.quizapp.model

import java.util.*

data class Record(
    val accuracy: Float,
    val duration: Long,
    val finishedAt: Date,
    val score: Int,
    val topic: String,
    val type: String,
    val userId: String,
)
