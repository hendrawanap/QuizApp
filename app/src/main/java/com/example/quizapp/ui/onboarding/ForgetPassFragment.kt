package com.example.quizapp.ui.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.example.quizapp.databinding.FragmentForgetPassBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth


class ForgetPassFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var _binding: FragmentForgetPassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val TAG = "ForgetPassFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentForgetPassBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            deleteBtn.setOnClickListener {
                deletePass()
            }
        }
        return binding.root
    }

    private fun deletePass() {
        var email = binding.emailLupa.text.toString()
        if (email.isNotEmpty()) {

            auth.sendPasswordResetEmail(email).addOnSuccessListener(OnSuccessListener { task ->
                    Toast.makeText(
                        this.context,
                        "Email untuk menghapus password telah berhasil dikirim",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.navigation_login)
            })
                .addOnFailureListener(OnFailureListener { exception ->
                    Toast.makeText(
                        this.context, exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                })
        } else {
            Toast.makeText(this.context, "Masukkan email terlebih dahulu!", Toast.LENGTH_LONG).show()

        }
    }
}