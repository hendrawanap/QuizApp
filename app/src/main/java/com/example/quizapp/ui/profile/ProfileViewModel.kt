package com.example.quizapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {
    private val _username = MutableLiveData<String>().apply {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            value = user.displayName
        }
    }
    val username: LiveData<String> = _username

    private val _nickname =  MutableLiveData<String>().apply{
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            value = user.displayName!!.split(" ")[0]
        }
    }

    val nickname: LiveData<String> =_nickname
    private val _email = MutableLiveData<String>().apply {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            value = user.email
        }
    }
    val email: LiveData<String> = _email

    private val _currentMenu = MutableLiveData<String>().apply {
        value =  "profil"
    }
    val currentMenu: LiveData<String> = _currentMenu

    fun updateProfileName(nama : String){
        val user = FirebaseAuth.getInstance().currentUser

        val profileNameUpdates = UserProfileChangeRequest.Builder().setDisplayName(nama).build()
        user!!.updateProfile(profileNameUpdates).addOnCompleteListener {
            _username.value = nama
            _nickname.value = nama.split(" ")[0]
        }
    }
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : LiveData<Boolean> = _isSuccess

    fun updateProfilePassword(passwordLama : String, passwordBaru : String){
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(user!!.email.toString(), passwordLama)
        user.reauthenticate(credential)
            .addOnFailureListener {
                _message.value = "Kata sandi salah"
                _isSuccess.value = false
            }
            .addOnSuccessListener {
            user!!.updatePassword(passwordBaru).addOnCompleteListener {
                _message.value =" Berhasil mengubah password"
                _isSuccess.value = true
            }
        }

    }
}