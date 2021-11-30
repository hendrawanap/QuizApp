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

    val user: LiveData<User> = _user
    val leaderboard: LiveData<MutableList<Leaderboard>> = _leaderboard
}