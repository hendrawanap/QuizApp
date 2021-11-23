package com.example.quizapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.quizapp.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            regBtn.setOnClickListener { register() }
            goToLoginBtn.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                finish()
            }
        }
    }

    private fun register() {
        var email = binding.regEmail.text.toString()
        var password = binding.regEmail.text.toString()
        var name = binding.regName.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
            if(task.isSuccessful){
                updateProfile(name)
            }else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateProfile(name: String, photoURL: String = "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_male_1.png?alt=media&token=be88df1f-7f78-425b-9d53-bea375677811") {
        val user = FirebaseAuth.getInstance().currentUser
        val newProfile = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse(photoURL))
            .build()
        user!!.updateProfile(newProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}