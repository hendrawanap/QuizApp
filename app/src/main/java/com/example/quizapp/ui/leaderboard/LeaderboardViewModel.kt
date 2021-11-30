package com.example.quizapp.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LeaderboardViewModel : ViewModel() {
    private val _leaderboardList = MutableLiveData<MutableList<Leaderboard>>()
    val leaderboardList: LiveData<MutableList<Leaderboard>> = _leaderboardList

    init {
        getLeaderboards()
    }

    fun getLeaderboards() {
        viewModelScope.launch(Dispatchers.IO) {
            val leaderboards = ArrayList<Leaderboard>()
            leaderboards.add(Leaderboard("Makanan - Pilgan", ArrayList()))
            leaderboards.add(Leaderboard("Makanan - Isian", ArrayList()))
            leaderboards.add(Leaderboard("Ikon - Pilgan", ArrayList()))
            leaderboards.add(Leaderboard("Ikon - Isian", ArrayList()))
            leaderboards.add(Leaderboard("Wisata - Pilgan", ArrayList()))
            leaderboards.add(Leaderboard("Wisata - Isian", ArrayList()))

            val makananLeaderboards = async { getLeaderboard("Makanan") }
            val ikonLeaderboards = async { getLeaderboard("Ikon") }
            val wisataLeaderboards = async { getLeaderboard("Wisata") }

            leaderboards[0] = makananLeaderboards.await()[0]
            leaderboards[1] = makananLeaderboards.await()[1]
            leaderboards[2] = ikonLeaderboards.await()[0]
            leaderboards[3] = ikonLeaderboards.await()[1]
            leaderboards[4] = wisataLeaderboards.await()[0]
            leaderboards[5] = wisataLeaderboards.await()[1]

            withContext(Dispatchers.Main) {
                _leaderboardList.value = leaderboards
            }
        }
    }

    private suspend fun getLeaderboard(topic: String) : ArrayList<Leaderboard> {
        val db = Firebase.firestore
        val collectionRef = db.collection("highestscores")
        val highestscores = collectionRef.whereEqualTo("topic", topic).get().await().toObjects(Highestscore::class.java)
        val leaderboards = ArrayList<Leaderboard>()
        leaderboards.add(Leaderboard("$topic - Pilgan", ArrayList()))
        leaderboards.add(Leaderboard("$topic - Isian", ArrayList()))

        if (highestscores.isNotEmpty()) {
            highestscores.sortByDescending { h -> h.score }

            var multiple = highestscores.filter { h -> h.category == "Multiple" }
            var short = highestscores.filter { h -> h.category == "Short" }

            if (multiple.isNotEmpty()) {
                var multipleUsers = multiple.map { m -> toUserLeaderboard(m)}
                leaderboards[0] = (Leaderboard("$topic - Pilgan", multipleUsers as ArrayList<UserLeaderboard>))
            }

            if (short.isNotEmpty()) {
                var shortUsers = short.map { m -> toUserLeaderboard(m)}
                leaderboards[1] = (Leaderboard("$topic - Isian", shortUsers as ArrayList<UserLeaderboard>))
            }
        }

        return leaderboards
    }

    private suspend fun toUserLeaderboard(highestScore: Highestscore): UserLeaderboard {
        val db = Firebase.firestore
        val usersRef = db.collection("users")
        val userData = usersRef.document(highestScore.userId).get().await()

        return UserLeaderboard(userData["username"].toString(), userData["displayImg"].toString(), highestScore.score)
    }
}