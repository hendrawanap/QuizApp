package com.example.quizapp.ui.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.R
import com.example.quizapp.model.Leaderboard
import com.example.quizapp.model.User
import com.example.quizapp.model.UserLeaderboard
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {


    private val _user = MutableLiveData<User>().apply{
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            value = User(user.uid, user.displayName!!, user.photoUrl.toString(), user.email!!)
        }
    }



    private val _leaderboard = MutableLiveData<MutableList<Leaderboard>>()

//    init{
//        val user1: UserLeaderboard = UserLeaderboard(R.mipmap.ic_launcher_round.toString(), 123)
//        val user2: UserLeaderboard = UserLeaderboard(R.mipmap.ic_launcher_round.toString(), 103)
//        val user3: UserLeaderboard = UserLeaderboard(R.mipmap.ic_launcher_round.toString(), 113)
//
//        val leaderboard1: Leaderboard = Leaderboard("Makanan", user1, user2, user3)
//        val leaderboard2: Leaderboard = Leaderboard("Ikon", user3, user1, user2)
//        val leaderboard3: Leaderboard = Leaderboard("Wisata", user2, user3, user1)
//
//        _leaderboard.value = mutableListOf<Leaderboard>(leaderboard1,leaderboard2,leaderboard3)
//    }

    val user: LiveData<User> = _user
    val leaderboard: LiveData<MutableList<Leaderboard>> = _leaderboard
}