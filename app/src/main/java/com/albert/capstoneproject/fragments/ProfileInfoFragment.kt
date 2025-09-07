package com.albert.capstoneproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentProfileInfoBinding
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth


// Fragment to display and update user profile information
class ProfileInfoFragment : Fragment() {

    private lateinit var binding: FragmentProfileInfoBinding // Binding object to access views in the FragmentProfileInfoBinding layout
    private val model: FireBaseViewModel by activityViewModels() // ViewModels to interact with data and Firebase
    private val viewModel: SharedViewModel by activityViewModels() // Inflate the layout for this fragment and return the root view

    // Inflate the layout for this fragment and return the root view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    // Called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the current user from Firebase Auth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Check if the user is logged in
        if (currentUser != null) {
            // Set the email hint in the UI
            binding.etEmail.hint = model.currentUser.value?.email ?: "No Email"

            // Observe personal info data from the SharedViewModel
            viewModel.personalInfo.observe(viewLifecycleOwner) { personalInfo ->
                // Find the personal info for the current user
                val currentUserInfo = personalInfo.find { it.user == currentUser.email }

                // Handle the click event for the "Save" button
                binding.btnSave.setOnClickListener {
                    // Get user input from the text fields
                    val firstName = binding.etName.text.toString().trim()
                    val lastName = binding.etLastName.text.toString().trim()

                    // Check if both fields are filled
                    if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                        // Create a PersonalInfo object with the updated information
                        val updateInfo = PersonalInfo(
                            infoId = currentUserInfo?.infoId
                                ?: 1, // Use existing infoId or default to 1
                            user = currentUser.email!!, // Current user email
                            userName = firstName, // Updated first name
                            userLastName = lastName, // Updated last name
                            userStreet = currentUserInfo?.userStreet
                                ?: "", // Retain existing street or default to empty
                            userHouseNumber = currentUserInfo?.userHouseNumber
                                ?: "", // Retain existing house number or default to empty
                            userCity = currentUserInfo?.userCity
                                ?: "", // Retain existing city or default to empty
                            userPostalCode = currentUserInfo?.userPostalCode
                                ?: "" // Retain existing postal code or default to empty
                        )
                        // Update the information in the ViewModel
                        viewModel.updateInfo(updateInfo)
                        Toast.makeText(requireContext(), "Saved!!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please enter both first name and last name!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // Populate the text fields with existing user information
                currentUserInfo?.let {
                    binding.etName.setText(it.userName)
                    binding.etLastName.setText(it.userLastName)
                }
            }

        } else {
            Toast.makeText(requireContext(), "Error: User is not logged in!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}