package com.example.quizapp.ui.onboarding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
            loginBtn.setOnClickListener {
                login()
                hideKeyboard()
            }
            goToRegBtn.setOnClickListener { findNavController().navigate(R.id.navigation_register) }
            lupaPass.setOnClickListener{findNavController().navigate(R.id.navigation_forgetPassword)}
        }

        return binding.root
    }

    private fun login() {
        binding.loginLoading.visibility = View.VISIBLE
        var email = binding.loginEmail.text.toString()
        var password = binding.loginPassword.text.toString()

        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener( OnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this.context, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    binding.loginLoading.visibility = View.GONE
                    val intent = Intent(this.activity, MainActivity::class.java)
                    startActivity(intent)
                }else {
                    Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_LONG).show()
                    binding.loginLoading.visibility = View.GONE
                }
            })
        } else {
            Toast.makeText(this.context, "Fill all the fields!", Toast.LENGTH_LONG).show()
            binding.loginLoading.visibility = View.GONE
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}