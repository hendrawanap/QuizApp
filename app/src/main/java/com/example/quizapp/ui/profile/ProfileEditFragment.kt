package com.example.quizapp.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.databinding.FragmentProfileEditBinding
import com.example.quizapp.ui.onboarding.OnboardingActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileEditFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileEditFragment()
    }

    private val viewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileEditBinding? = null
    private lateinit var message: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        binding.toggle.apply {
            left.text = "Profil"
            right.text = "Kata Sandi"
            toggle.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    left.id -> {
                        binding.profileName.root.visibility = View.VISIBLE
                        binding.profilePassword.root.visibility = View.GONE
                    }
                    right.id -> {
                        binding.profileName.root.visibility = View.GONE
                        binding.profilePassword.root.visibility = View.VISIBLE
                    }

                }
            }
        }
        binding.profileName.apply {
            editButtonName.setOnClickListener {
                inputNama.isFocusable = !inputNama.isFocusable
                inputNama.isFocusableInTouchMode = !inputNama.isFocusableInTouchMode
                if(inputNama.isFocusable == true){
                    editButtonName.setImageResource(R.drawable.ic_baseline_close_24)
                }else{
                    editButtonName.setImageResource(R.drawable.ic_baseline_edit_24)
                }
            }
            simpanButton.setOnClickListener {
                viewModel.updateProfileName(binding.profileName.inputNama.text.toString())
            }
        }
        binding.profilePassword.apply {
            editButtonPassword.setOnClickListener {
                inputPassword.isFocusable = !inputPassword.isFocusable
                inputPassword.isFocusableInTouchMode = !inputPassword.isFocusableInTouchMode
                inputPasswordBaru.isFocusable = !inputPasswordBaru.isFocusable
                inputPasswordBaru.isFocusableInTouchMode = !inputPasswordBaru.isFocusableInTouchMode
                if(inputPassword.isFocusable == true){
                    editButtonPassword.setImageResource(R.drawable.ic_baseline_close_24)
                }else{
                    editButtonPassword.setImageResource(R.drawable.ic_baseline_edit_24)
                }
            }
            simpanButton.setOnClickListener {
                viewModel.updateProfilePassword(binding.profilePassword.inputPassword.text.toString(),binding.profilePassword.inputPasswordBaru.text.toString())
            }
        }
        viewModel.apply {
            message.observe(viewLifecycleOwner, Observer {
                this@ProfileEditFragment.message = it
            })
            isSuccess.observe(viewLifecycleOwner, Observer {
                Toast.makeText(this@ProfileEditFragment.context,this@ProfileEditFragment.message,Toast.LENGTH_SHORT).show()
            })
            user.observe(viewLifecycleOwner, Observer {
                binding.namaPengguna.text = it.username
                binding.profileName.inputNama.setText(it.username)
                binding.profileName.inputEmail.setText(it.email)
                Glide.with(this@ProfileEditFragment).load(it.displayImg).into(binding.profileImage)
            })
        }
        return binding.root
    }

}