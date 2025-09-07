package com.albert.capstoneproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentAddressInfoBinding
import com.albert.capstoneproject.databinding.FragmentOrderInfoBinding
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth


// Fragment to handle Address Information
class AddressInfoFragment : Fragment() {
    private lateinit var binding: FragmentAddressInfoBinding // Binding object for the layout
    private val model: FireBaseViewModel by activityViewModels() // ViewModel to handle Firebase operations
    private val viewModel: SharedViewModel by activityViewModels() // SharedViewModel to store and manage UI-related data

    // onCreate method, part of Fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Inflate the view for this fragment using FragmentAddressInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding object with the layout
        binding = FragmentAddressInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    // Called when the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the currently logged-in user from FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Check if the user is logged in
        if (currentUser != null) {
            // Observe the personalInfo LiveData from the SharedViewModel
            viewModel.personalInfo.observe(viewLifecycleOwner) { personalInfo ->

                // Find the user's personal information in the list of personalInfo objects
                val currentUserInfo = personalInfo.find { it.user == currentUser.email }

                // Set click listener for the save button
                binding.btnSave.setOnClickListener {
                    // Get the text input from the EditText fields
                    val street = binding.etStreet.text.toString().trim()
                    val houseNumber = binding.etHouseNumber.text.toString().trim()
                    val city = binding.etCity.text.toString().trim()
                    val postalCode = binding.etPostalCode.text.toString().trim()

                    // Check if all fields are filled out
                    if (street.isNotEmpty() && houseNumber.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty()) {
                        // Create a new PersonalInfo object with the updated data
                        val updateInfo = PersonalInfo(
                            infoId = currentUserInfo?.infoId
                                ?: 1, // Assign an ID if the user doesn't have one
                            user = currentUser.email!!, // Use the current user's email
                            userName = currentUserInfo?.userName
                                ?: "", // Get other info from currentUserInfo, if it exists
                            userLastName = currentUserInfo?.userLastName
                                ?: "", // Get other info from currentUserInfo, if it exists
                            userStreet = street, // Updated street
                            userHouseNumber = houseNumber, // Updated house number
                            userCity = city, // Updated city
                            userPostalCode = postalCode // Updated postal code
                        )
                        // Call the ViewModel function to update the user's info in the database
                        viewModel.updateInfo(updateInfo)
                        Toast.makeText(requireContext(), "Saved!!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(), "Please fill all your Info!!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // If currentUserInfo exists, pre-fill the fields with the user's existing information
                currentUserInfo?.let {
                    binding.etStreet.setText(it.userStreet)
                    binding.etHouseNumber.setText(it.userHouseNumber)
                    binding.etCity.setText(it.userCity)
                    binding.etPostalCode.setText(it.userPostalCode)
                }
            }

        } else {
            Toast.makeText(requireContext(), "Error: User is not logged in!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}