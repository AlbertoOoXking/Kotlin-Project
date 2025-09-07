package com.albert.capstoneproject.FireBase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentStartScreenBinding

class StartScreenFragment : Fragment() {

    private lateinit var binding: FragmentStartScreenBinding
    private val viewModel: FireBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }
}