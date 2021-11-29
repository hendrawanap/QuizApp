package com.example.quizapp.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private lateinit var rvAdapter: RecentlyPlayedAdapter
    private lateinit var recentlyPlayedList : ArrayList<Record>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        recentlyPlayedList = arrayListOf(
            Record(0.5f,1000L, Date(),3540, "Makanan", "Multiple", "234112323" ),
            Record(0.5f,1000L, Date(),3240, "Ikon", "Short", "23412323" ),
            Record(0.5f,1000L, Date(),3140, "Wisata", "Multiple", "2341231231" ),
            Record(0.5f,1000L, Date(),2540, "Makanan", "Short", "234123123" ),
            Record(0.5f,1000L, Date(),5540, "Wisata", "Multiple", "2312314123" ),
            Record(0.5f,1000L, Date(),3740, "Ikon", "Short", "234123123" )

        )
        rvAdapter = RecentlyPlayedAdapter(recentlyPlayedList)
        binding.rvRecentlyPlayed.rvProfile.adapter = rvAdapter

        binding.editButton.setOnClickListener { findNavController().navigate(R.id.navigation_profile_edit) }
//
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