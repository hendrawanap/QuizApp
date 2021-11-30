package com.example.quizapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.example.quizapp.model.Record
import com.example.quizapp.model.User
import com.google.firebase.firestore.ktx.firestore
import kotlin.collections.ArrayList

class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<User>().apply{
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            value = User(user.uid, user.displayName!!, user.photoUrl.toString(), user.email!!)
        } else {
            value = User("", "", "", "")
        }
    }
    val user: LiveData<User> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    private val _username = MutableLiveData<String>().apply {
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            value = user.displayName
//        }
//    }
//    val username: LiveData<String> = _username
//
//    private val _nickname =  MutableLiveData<String>().apply{
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            value = user.displayName!!.split(" ")[0]
//        }
//    }
//
//    val nickname: LiveData<String> =_nickname
//    private val _email = MutableLiveData<String>().apply {
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            value = user.email
//        }
//    }
//    val email: LiveData<String> = _email

    private val _currentMenu = MutableLiveData<String>().apply {
        value =  "profil"
    }
    val currentMenu: LiveData<String> = _currentMenu

    fun updateProfileName(nama : String){
        val user = FirebaseAuth.getInstance().currentUser

        val profileNameUpdates = UserProfileChangeRequest.Builder().setDisplayName(nama).build()
        user!!.updateProfile(profileNameUpdates).addOnCompleteListener {
            _user.value = User(user.uid,nama,user.photoUrl.toString(),user.email!!)
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

    private val _history = MutableLiveData<MutableList<Record>>()
    private val _newhistory = MutableLiveData<MutableList<Record>>()

    init{
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
            getRecords()
        }
    }


    fun getRecords() {
        _isLoading.value = true
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser
        val collectionRef = db.collection("records")
        val records = ArrayList<Record>()
        collectionRef
            .whereEqualTo("userId", user!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    records.add(document.toObject(Record::class.java))
                }
                records.sortByDescending { t -> t.finishedAt }
                _history.value = records
                if(records.size < 3){
                    _newhistory.value = records
                } else {
                    _newhistory.value = records.subList(0, 3)
                }
                _isLoading.value = false
            }
    }



    val history: LiveData<MutableList<Record>> = _history
    val newhistory: LiveData<MutableList<Record>> = _newhistory
}