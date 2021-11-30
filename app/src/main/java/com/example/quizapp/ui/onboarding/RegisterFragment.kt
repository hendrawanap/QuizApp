package com.example.quizapp.ui.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentRegisterBinding
import com.example.quizapp.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.apply {
            regBtn.setOnClickListener { register() }
            goToLoginBtn.setOnClickListener { findNavController().navigate(R.id.navigation_login) }
        }

        return binding.root
    }

    private fun register() {
        var email = binding.regEmail.text.toString()
        var password = binding.regPassword.text.toString()
        var name = binding.regName.text.toString()

        if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( OnCompleteListener{ task ->
                if(task.isSuccessful){
                    updateProfile(name)
                }else {
                    Toast.makeText(this.context, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this.context, "Fill all the fields!", Toast.LENGTH_LONG).show()
        }

    }

    private fun updateProfile(name: String) {
        val photoUrls = arrayListOf<String>(
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_1.png?alt=media&token=3a610561-ef98-427e-8c21-59d31f632030",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_10.png?alt=media&token=96619e75-d54e-4f98-8f03-fc41622a0eb7",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_2.png?alt=media&token=2fde9511-9b30-466e-bca8-016f2f128ccd",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_3.png?alt=media&token=160a9cad-9bdf-4d9f-801c-665d6d26644d",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_4.png?alt=media&token=37915b41-d1be-4e1c-9952-fbc977d727eb",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_5.png?alt=media&token=11368dab-30f6-4620-a772-58c96b7ae900",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_6.png?alt=media&token=65cfdf7b-20a3-45e5-b3ef-62ca4438403e",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_7.png?alt=media&token=c92e4d86-d8fa-489e-aa39-87c349e35f86",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_8.png?alt=media&token=747d5b06-2a5a-4a58-a14c-99c051a2b650",
            "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_9.png?alt=media&token=c3106d98-7c69-456e-8daf-17f2bbeb93b1"
        )
        val user = FirebaseAuth.getInstance().currentUser
        val photoUri = photoUrls[(0..9).random()]
        val newProfile = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse(photoUri))
            .build()
        user!!.updateProfile(newProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                addToDatabase(User(user.uid, name, photoUri, user.email!!))
                Toast.makeText(this.context, "Successfully Registered", Toast.LENGTH_LONG).show()
                val intent = Intent(this.activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun addToDatabase(user: User) {
        val db = Firebase.firestore
        val collectionRef = db.collection("users")
        collectionRef.document(user.uid).set(user)
    }

}