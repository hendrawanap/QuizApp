package com.example.quizapp.model

data class Leaderboard(
    val topic:String,
    val users: ArrayList<UserLeaderboard>,
)
