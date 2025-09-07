package com.albert.capstoneproject.FireBase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentRegisterBinding
import com.albert.capstoneproject.viewModel.SharedViewModel


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding // Binding object for the fragment's layout
    private val viewModel: FireBaseViewModel by activityViewModels() // ViewModel for Firebase authentication
    private val sharedViewModel: SharedViewModel by activityViewModels() // Shared ViewModel for handling personal information
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun String.isEmailValid(): Boolean {
        // Regex to validate email format
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the click listener for the Sign Up button
        binding.btnSignUp.setOnClickListener {

            val email = binding.etEmail.text.toString() // Get the email input from the user
            val password = binding.etPassword.text.toString() // Get the password input from the user
            val repPassword = binding.etRepPassword.text.toString() // Get the repeated password input from the user

            // Check if all fields are filled, passwords match, and email is valid
            if (email.isNotEmpty() && password.isNotEmpty() && password == repPassword && email.isEmailValid()) {
                // Call the signup function from FireBaseViewModel
                viewModel.signup(email, password)
                // Insert personal info into the shared ViewModel
                sharedViewModel.insertInfo(PersonalInfo(user = email))
            } else {
                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cvFacebook.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        // Observe the currentUser LiveData from FireBaseViewModel
        viewModel.currentUser.observe(viewLifecycleOwner) {
            // If a user is successfully registered, navigate to homeFragment
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }
}