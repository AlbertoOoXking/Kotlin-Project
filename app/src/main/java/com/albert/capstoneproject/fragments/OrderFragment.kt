package com.albert.capstoneproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.albert.capstoneproject.Adapter.OrderAdapter
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentOrderBinding
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth


// Fragment to display a list of orders placed by the user
class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding // Binding object to access the views in the FragmentOrderBinding layout
    private val viewModel: SharedViewModel by activityViewModels() // SharedViewModel to manage shared data like the list of orders
    private lateinit var orderAdapter: OrderAdapter // Adapter for populating the order list in the RecyclerView
    private val model: FireBaseViewModel by activityViewModels() // Firebase ViewModel for accessing Firebase data

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding object with the layout inflater
        binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    // Called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.tv_titlo).text = "Order"

        // Get the current logged-in user from FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Observe the list of orders from the SharedViewModel
        viewModel.orderList.observe(viewLifecycleOwner) { orderList ->
            // Filter the orders to only include those belonging to the current user
            val filteredList = orderList.filter {
                it.user == currentUser!!.email
            }
            // Reverse the order list so the latest orders are shown first
            val reversedList = filteredList.reversed()
            // Initialize the OrderAdapter with the filtered and reversed list
            orderAdapter = OrderAdapter(model, reversedList)
            // Set the adapter to the RecyclerView in the layout
            binding.rvOrder.adapter = orderAdapter
        }
    }
}