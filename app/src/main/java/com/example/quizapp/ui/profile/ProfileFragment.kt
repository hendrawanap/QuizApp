package com.example.quizapp.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.model.Record
import com.example.quizapp.ui.onboarding.OnboardingActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var history : MutableList<Record>
    private lateinit var rvAdapter: RecentlyPlayedAdapter
   private lateinit var recentlyPlayedList : ArrayList<Record>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.getRecords()
        history = mutableListOf<Record>()
        viewModel.history.observe(viewLifecycleOwner, {
            history = it
            binding.rvRecentlyPlayed.rvProfile.adapter = RecentlyPlayedAdapter(history)
        })

        binding.editButton.setOnClickListener { findNavController().navigate(R.id.navigation_profile_edit) }

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this.activity, OnboardingActivity::class.java)
            startActivity(intent)
        }
        viewModel.nickname.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.username.text = it
        })
        viewModel.email.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.tvEmail.text = it
        })



        return binding.root
    }

}