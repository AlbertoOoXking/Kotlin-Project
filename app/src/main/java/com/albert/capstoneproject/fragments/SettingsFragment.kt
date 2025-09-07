package com.albert.capstoneproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentSettingsBinding
import com.albert.capstoneproject.viewModel.SharedViewModel

// Fragment to display settings options and handle navigation
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding // Binding object to access views in the FragmentSettingsBinding layout
    // ViewModels for handling Firebase operations and shared data
    private val firebaseViewModel: FireBaseViewModel by activityViewModels()
    private val viewModel: SharedViewModel by activityViewModels()

    // Inflate the layout for this fragment and return the root view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    // Called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.tv_titlo).text = "Settings"

        // Fetch user personal information from the ViewModel
        viewModel.getInfo()
        Log.e("SETTINGS PERSONALINFO", viewModel.personalInfo.value.toString())

        // Handle click event for the "Log Out" card view
        binding.cvLogOut.setOnClickListener {
            // Call logOut method in FireBaseViewModel to log out the user
            firebaseViewModel.logOut()
            // Navigate to the start screen after logging out
            findNavController().navigate(R.id.startScreenFragment)
        }
        // Handle click event for the "Profile" card view
        binding.cvProfile.setOnClickListener {
            // Navigate to the ProfileInfoFragment
            findNavController().navigate(R.id.profileInfoFragment)
        }
        // Handle click event for the "Address" card view
        binding.cvAddress.setOnClickListener {
            // Navigate to the AddressInfoFragment
            findNavController().navigate(R.id.addressInfoFragment)
        }
        // Handle click event for the "Favourites" card view
        binding.cvFavourites.setOnClickListener {
            // Navigate to the FavouritesFragment
            findNavController().navigate(R.id.favouritesFragment)
        }
    }
}