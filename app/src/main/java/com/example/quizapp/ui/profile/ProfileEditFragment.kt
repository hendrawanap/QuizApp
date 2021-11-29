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

        binding.toggle.left.text = "Profil"
        binding.toggle.right.text = "Kata Sandi"
        binding.toggle.toggle.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.toggle.left.id -> {
                    binding.profileName.root.visibility = View.VISIBLE
                    binding.profilePassword.root.visibility = View.GONE
                }
                binding.toggle.right.id -> {
                    binding.profileName.root.visibility = View.GONE
                    binding.profilePassword.root.visibility = View.VISIBLE
                }

            }
        }
        binding.profileName.editButtonName.setOnClickListener {
            binding.profileName.inputNama.isFocusable = !binding.profileName.inputNama.isFocusable
            binding.profileName.inputNama.isFocusableInTouchMode = !binding.profileName.inputNama.isFocusableInTouchMode
//            when(binding.profileName.inputNama.isFocusable){
//                true -> {
//                    binding.profileName.editButtonName.setBackgroundResource(R.drawable.ic_baseline_close_24)
//                }false -> {
//                    binding.profileName.editButtonName.setBackgroundResource(R.drawable.ic_baseline_edit_24)
//                }
//            }
            if(binding.profileName.inputNama.isFocusable == true){
                binding.profileName.editButtonName.setImageResource(R.drawable.ic_baseline_close_24)
            }else{
                binding.profileName.editButtonName.setImageResource(R.drawable.ic_baseline_edit_24)
            }
        }
        binding.profilePassword.editButtonPassword.setOnClickListener {
            binding.profilePassword.inputPassword.isFocusable = !binding.profilePassword.inputPassword.isFocusable
            binding.profilePassword.inputPassword.isFocusableInTouchMode = !binding.profilePassword.inputPassword.isFocusableInTouchMode
            binding.profilePassword.inputPasswordBaru.isFocusable = !binding.profilePassword.inputPasswordBaru.isFocusable
            binding.profilePassword.inputPasswordBaru.isFocusableInTouchMode = !binding.profilePassword.inputPasswordBaru.isFocusableInTouchMode
//            when(binding.profilePassword.inputPassword.isFocusable){
//                true -> {
//                    binding.profilePassword.editButtonPassword.setBackgroundResource(R.drawable.ic_baseline_close_24)
//                }false -> {
//                    binding.profilePassword.editButtonPassword.setBackgroundResource(R.drawable.ic_baseline_edit_24)
//                }
//            }
            if(binding.profilePassword.inputPassword.isFocusable == true){
                binding.profilePassword.editButtonPassword.setImageResource(R.drawable.ic_baseline_close_24)
            }else{
                binding.profilePassword.editButtonPassword.setImageResource(R.drawable.ic_baseline_edit_24)
            }
        }
        binding.profileName.simpanButton.setOnClickListener {
            viewModel.updateProfileName(binding.profileName.inputNama.text.toString())
        }
        viewModel.username.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.profileName.inputNama.setText(it)
            binding.namaPengguna.text = it
        })
        viewModel.email.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.profileName.inputEmail.setText(it)
        })
        binding.profilePassword.simpanButton.setOnClickListener {
            viewModel.updateProfilePassword(binding.profilePassword.inputPassword.text.toString(),binding.profilePassword.inputPasswordBaru.text.toString())
        }
        viewModel.message.observe(viewLifecycleOwner, Observer {
            message = it
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show()
        })


        return binding.root
    }

}