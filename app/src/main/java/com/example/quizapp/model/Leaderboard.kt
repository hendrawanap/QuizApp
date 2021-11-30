package com.example.quizapp.model

data class Leaderboard(val topic:String,
                       val user1: UserLeaderboard,
                       val user2: UserLeaderboard,
                       val user3: UserLeaderboard)
