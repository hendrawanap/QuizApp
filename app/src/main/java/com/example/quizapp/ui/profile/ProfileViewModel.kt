package com.example.quizapp.ui.profile

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.example.quizapp.R
import com.example.quizapp.model.Record
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import java.util.*
import kotlin.collections.ArrayList

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

    private val _history = MutableLiveData<MutableList<Record>>()
    private val _newhistory = MutableLiveData<MutableList<Record>>()

    init{
        val history1: Record = Record(0.5f,1000L, Date(),3540, "Makanan", "Short", "234112323" )
        val history2: Record = Record(0.5f,1000L, Date(),3540, "Wisata", "Multiple", "234112323" )
        val history3: Record = Record(0.5f,1000L, Date(),3240, "Ikon", "Short", "23412323" )
        val history4: Record = Record(0.5f,1000L, Date(),3540, "Makanan", "Multiple", "234112323" )
        val history5: Record = Record(0.5f,1000L, Date(),3740, "Ikon", "Short", "234123123")
        val history6: Record = Record(0.5f,1000L, Date(),3540, "Wisata", "Short", "234112323" )


        val tempnew =  mutableListOf<Record>(history1,history2,history3,history4,history5,history6)

        if (tempnew != null) {
            if(tempnew.size > 3){
                val index = tempnew!!.size - 4
                for(i in 0..index){
                    tempnew.removeAt(3)
                }
                _newhistory.value = tempnew!!
            } else{
                _newhistory.value = tempnew!!
            }
        }


    }


    fun getRecords(){
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser
        val collectionRef = db.collection("records")
        val records = ArrayList<Record>()
        collectionRef
            .whereEqualTo("userId", user!!.uid)
//            .orderBy("finishedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    records.add(document.toObject(Record::class.java))
                }
                _history.value = records
                if(records.size < 3){
                    _newhistory.value = records
                } else {
                    _newhistory.value = records.subList(0, 3)
                }
            }
    }



    val history: LiveData<MutableList<Record>> = _history
    val newhistory: LiveData<MutableList<Record>> = _newhistory
}