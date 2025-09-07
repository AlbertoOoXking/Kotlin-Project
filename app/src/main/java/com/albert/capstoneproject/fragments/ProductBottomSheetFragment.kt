package com.albert.capstoneproject.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentOrderOptionsBinding
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


// BottomSheetFragment for displaying product options like size, color, and quantity before adding to the cart
class ProductBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentOrderOptionsBinding // View binding for the FragmentOrderOptionsBinding layout
    private val model: FireBaseViewModel by activityViewModels() // Firebase ViewModel for accessing user data and interacting with Firebase
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel to manage product and cart-related data

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Inflate the layout and return the binding's root view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderOptionsBinding.inflate(layoutInflater)
        return binding.root
    }

    // This method is called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the item count and update the TextView that displays the number of items
        viewModel.itemCount.observe(viewLifecycleOwner) {
            binding.tvNumberOfItems.text = it.toString()
        }
        // Handle the click event for the "+" button to increase the item count
        binding.btnPlus.setOnClickListener {
            viewModel.plusArticle()
        }
        // Handle the click event for the "-" button to decrease the item count
        binding.btnMinus.setOnClickListener {
            viewModel.minusArticle()
        }
        // Confirm button listener to add the selected product with options (color, size, quantity) to the cart
        binding.btnConfirm.setOnClickListener {
            // Determine the selected color from the radio group
            val selectedColor = when (binding.rgColors.checkedRadioButtonId) {
                R.id.rb_red -> "Red"
                R.id.rb_blue -> "Blue"
                R.id.rb_green -> "Green"
                else -> "No color"
            }
            // Determine the selected size from the radio group
            val selectedSize = when (binding.rgSize.checkedRadioButtonId) {
                R.id.rb_small -> "Small"
                R.id.rb_medium -> "Medium"
                R.id.rb_large -> "Large"
                else -> "No size"
            }
            // Get the chosen number of items from the TextView
            val chosenNumber = binding.tvNumberOfItems.text.toString().toInt()
            // Observe the current product data to add to the cart
            viewModel.currentProduct.observe(viewLifecycleOwner) { product ->
                // Create a CartItems object with the selected options
                val addToCart = CartItems(
                    productName = product.title,
                    price = product.price.toString(),
                    image = product.image,
                    user = model.currentUser.value!!.email!!, // Current logged-in user
                    count = chosenNumber, // Number of items selected
                    selectedColor = selectedColor, // Chosen color
                    selectedSize = selectedSize // Chosen size
                )
                // Insert the CartItems object into the cart
                viewModel.insertCart(addToCart)
                Toast.makeText(requireContext(), "Add to cart!", Toast.LENGTH_SHORT).show()
                // Reset the article count back to 1
                viewModel.resetArticle()
            }
            // Dismiss the BottomSheet after adding to the cart
            dismiss()
        }
    }

    // Customizes the behavior of the BottomSheetDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        // Expand the bottom sheet when it's shown
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            // Ensure the BottomSheet expands fully
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    // Companion object to define the fragment tag
    companion object {
        const val TAG = "BottomSheetFragment"
    }
}