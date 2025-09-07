package com.albert.capstoneproject.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.albert.capstoneproject.Adapter.CartAdapter
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.albert.capstoneproject.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import okhttp3.internal.format

class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding // Binding object for the fragment's layout
    private val viewModel: SharedViewModel by activityViewModels() // ViewModel for managing cart data
    private lateinit var cartAdapter: CartAdapter // Adapter for the RecyclerView
    private val model: FireBaseViewModel by activityViewModels() // ViewModel for Firebase operations

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.tv_titlo).text = "Cart"

        val currentUser =
            FirebaseAuth.getInstance().currentUser // Get the current user from Firebase

        // Observe the cartList LiveData from SharedViewModel
        viewModel.cartList.observe(viewLifecycleOwner) { cartItemsList ->
            // Filter and reverse the cart items list to show only items for the current user
            val filteredList = cartItemsList.filter {
                it.user == currentUser!!.email
            }
            val reversedList = filteredList.reversed()
//            if (reversedList.isEmpty()) {
//                binding.ivBackgroundLogo.visibility = View.VISIBLE
//                binding.tvOverall.visibility = View.GONE
//                binding.total.visibility = View.GONE
//            } else {
//                binding.ivBackgroundLogo.visibility = View.GONE
//                binding.tvOverall.visibility = View.VISIBLE
//                binding.total.visibility = View.VISIBLE

            // Initialize the cart adapter with the filtered and reversed list
            cartAdapter = CartAdapter(model, reversedList.toMutableList(), viewModel, this)
            binding.rvCart.adapter = cartAdapter

            // Set up swipe-to-delete functionality for the RecyclerView
            val swipeToDeleteCallback = SwipeToDeleteCallback()
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(binding.rvCart)

            // Calculate and display the total price
            val total = calculate(reversedList)
            binding.total.text = total.toString()

            // Show or hide the update button based on whether there are items to update
            if (viewModel.toUpdate.isNotEmpty()) {
                binding.btnDone.visibility = View.VISIBLE
                binding.btnOrder.isEnabled = false
            } else {
                binding.btnDone.visibility = View.GONE
                binding.btnOrder.isEnabled = total > 0
            }

            // Set up click listener for the order button
            binding.btnOrder.setOnClickListener {
                if (total > 0 && viewModel.toUpdate.isEmpty()) {
                    Log.e("CartFragment", "Total: $total")
                    setUpSheet() // Proceed to order sheet
                } else {
                    binding.btnOrder.isEnabled = false
                }
            }

            // Set up click listener for the update button
            binding.btnDone.setOnClickListener {
                // Update items and clear the update list
                for (product in viewModel.toUpdate) {
                    viewModel.update(product)
                }
                viewModel.toUpdate.clear()
                binding.btnDone.visibility = View.GONE

                // Enable the order button if the total is greater than zero
                if (total > 0) {
                    binding.btnOrder.isEnabled = true
                }
            }
            //}
        }
    }

    private fun setUpSheet() {
        // Show the order bottom sheet fragment
        binding.apply {
            val sheet = OrderBottomSheetFragment()
            sheet.show(parentFragmentManager, OrderBottomSheetFragment.TAG)
        }
    }

    private fun calculate(cartItems: List<CartItems>): Double {
        // Calculate the total price of the cart items
        var total = 0.0
        for (item in cartItems) {
            val itemPrice = item.price.toDouble()
            val itemCount = item.count
            total += itemPrice * itemCount
        }
        val formattedTotal = format("%.2f", total).toDouble()
        viewModel.updateTotalPrice(formattedTotal)
        return formattedTotal
    }

    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val cartAdapter = binding.rvCart.adapter as? CartAdapter

            // Create an AlertDialog to confirm item deletion
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.apply {
                setTitle("Delete item")
                setMessage("Are you sure you want to delete this item?")
                setPositiveButton("Yes") { dialog, _ ->
                    val deletedItem = cartAdapter?.getItem(position)
                    deletedItem?.let { viewModel.delete(it) }
                    cartAdapter?.removeItem(position)
                    dialog.dismiss()
                }
                setNegativeButton("No") { dialog, _ ->
                    // Notify adapter that item was not deleted
                    this@CartFragment.cartAdapter.notifyItemChanged(position)
                    dialog.dismiss()
                }
//                setOnDismissListener {
//                    cartAdapter?.notifyItemChanged(position)
//                }
                create().show()
            }
        }
    }
}