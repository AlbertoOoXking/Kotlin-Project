package com.albert.capstoneproject.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.databinding.FragmentOrderInfoBinding
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


// Fragment representing the BottomSheet dialog for order confirmation
class OrderBottomSheetFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentOrderInfoBinding // View binding for the FragmentOrderInfoBinding layout
    private val model: FireBaseViewModel by activityViewModels() // Firebase ViewModel for accessing user data
    private val viewModel: SharedViewModel by activityViewModels() // Shared ViewModel for managing UI-related data (like cart and personal info)

    // Lifecycle method: called when the fragment is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Inflate the view using the FragmentOrderInfoBinding layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    // This method is called when the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the email hint in the EditText using the current user's email from FirebaseViewModel
        binding.etEmail.hint = model.currentUser.value!!.email.toString()

        // Observe the personal info data from SharedViewModel
        viewModel.personalInfo.observe(viewLifecycleOwner) { personalInfoList ->

            // Get the current user's email from the FirebaseViewModel
            val currentUserEmail = model.currentUser.value?.email

            // Find the user's personal information using their email
            val currentUserInfo = personalInfoList.find { it.user == currentUserEmail }

            // If the user has personal info, populate the input fields with the existing data
            currentUserInfo?.let { info ->
                binding.etName.setText(info.userName)
                binding.etLastName.setText(info.userLastName)
                binding.etStreet.setText(info.userStreet)
                binding.etCity.setText(info.userCity)
                binding.etPostalCode.setText(info.userPostalCode)
            }
        }

        // Observe cart items and total price from SharedViewModel
        viewModel.cartList.observe(viewLifecycleOwner) { cartItems ->
            viewModel.totalPrice.observe(viewLifecycleOwner) { totalPrice ->

                // Set an OnClickListener for the confirm button
                binding.btnConfirm.setOnClickListener {
                    // Get the input values from the EditText fields
                    val name = binding.etName.text.toString().trim()
                    val lastName = binding.etLastName.text.toString().trim()
                    val street = binding.etStreet.text.toString().trim()
                    val city = binding.etCity.text.toString().trim()
                    val postalCode = binding.etPostalCode.text.toString().trim()

                    // Check if all required fields are filled
                    if (name.isNotEmpty() && lastName.isNotEmpty() && street.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty()) {
                        // Insert the order into the database using ViewModel
                        viewModel.insertOrder(cartItems, totalPrice)
                        Toast.makeText(requireContext(), "Order confirmed!", Toast.LENGTH_SHORT)
                            .show()

                        // After confirming the order, clear the cart
                        for (item in cartItems) {
                            viewModel.delete(item)
                        }

                        // Dismiss the BottomSheet
                        dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(), "Please fill your info", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // Set an OnClickListener for the cancel button to dismiss the bottom sheet
                binding.tvCancel.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    // This method customizes the dialog when the bottom sheet is shown
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Set a listener to expand the BottomSheet when it is shown
        dialog.setOnShowListener {

            // Find the bottom sheet view and set its behavior to expand fully
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as LinearLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            // Set the height of the peeked bottom sheet to the full height of the layout
            bottomSheetBehavior.peekHeight = bottomSheet.height

        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }
}