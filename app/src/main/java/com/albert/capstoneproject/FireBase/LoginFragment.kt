package com.albert.capstoneproject.FireBase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentLoginBinding
import com.albert.capstoneproject.viewModel.SharedViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding // Binding object for the fragment's layout
    private val viewModel: FireBaseViewModel by activityViewModels() // ViewModel for handling Firebase authentication
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the click listener for the login button
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString() // Get the email input from the user
            val password =
                binding.etPassword.text.toString() // Get the password input from the user

            // Check if both email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the login function from the ViewModel
                viewModel.login(email, password)
                sharedViewModel.insertInfo(PersonalInfo(user = email))
            } else {
                // Show a toast message if the email or password fields are empty
                Toast.makeText(context, "Please fill in", Toast.LENGTH_SHORT).show()
            }
        }
        // Observe the currentUser LiveData from the ViewModel
        viewModel.currentUser.observe(viewLifecycleOwner) {
            // If a user is successfully logged in, navigate to the home fragment
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }
}