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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

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

    private fun updateProfile(name: String, photoURL: String = "https://firebasestorage.googleapis.com/v0/b/quiz-app-99856.appspot.com/o/avatars%2Favatar_male_1.png?alt=media&token=be88df1f-7f78-425b-9d53-bea375677811") {
        val user = FirebaseAuth.getInstance().currentUser
        val newProfile = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse(photoURL))
            .build()
        user!!.updateProfile(newProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this.context, "Successfully Registered", Toast.LENGTH_LONG).show()
                val intent = Intent(this.activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}