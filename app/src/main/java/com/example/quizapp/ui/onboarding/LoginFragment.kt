package com.example.quizapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            loginBtn.setOnClickListener { login() }
            goToRegBtn.setOnClickListener { findNavController().navigate(R.id.navigation_register) }
        }

        return binding.root
    }

    private fun login() {
        var email = binding.loginEmail.text.toString()
        var password = binding.loginPassword.text.toString()

        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener( OnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this.context, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    val intent = Intent(this.activity, MainActivity::class.java)
                    startActivity(intent)
                }else {
                    Log.d(TAG, email)
                    Log.d(TAG, password)
                    Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this.context, "Fill all the fields!", Toast.LENGTH_LONG).show()
        }
    }
}