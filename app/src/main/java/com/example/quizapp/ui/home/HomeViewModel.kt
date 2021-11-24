package com.example.quizapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val _username = MutableLiveData<String>().apply {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            value = user.displayName
        }
    }
    val username: LiveData<String> = _username
}