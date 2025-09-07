package com.albert.capstoneproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.CartListItemBinding
import com.albert.capstoneproject.fragments.CartFragment
import com.albert.capstoneproject.viewModel.SharedViewModel


/**
 * Adapter for displaying cart items in a RecyclerView.
 *
 * @param model Instance of FireBaseViewModel used for user-related operations.
 * @param cartItems List of CartItems to display in the RecyclerView.
 * @param viewModel Instance of SharedViewModel used for managing cart operations.
 * @param cartFragment Instance of CartFragment to interact with UI elements in the fragment.
 */
class CartAdapter(
    private val model: FireBaseViewModel,
    private var cartItems: MutableList<CartItems>,
    private val viewModel: SharedViewModel,
    private val cartFragment: CartFragment
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding the views of each cart item.
     *
     * @param binding Binding object for the cart item layout.
     */
    inner class ViewHolder(val binding: CartListItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the layout for each cart item and returns a ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The type of view to create.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Returns the number of cart items in the list.
     *
     * @return The number of items in the cart.
     */
    override fun getItemCount(): Int {
        return cartItems.size
    }

    /**
     * Binds the data for a specific cart item to the ViewHolder.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]

        // Check if the current item belongs to the logged-in user.
        if (cartItem.user == model.currentUser.value!!.email) {
            // Set product name, price, size, color, and quantity in the UI.
            holder.binding.ivCartProduct.load(cartItem.image)
            holder.binding.tvProductName.text = cartItem.productName
            holder.binding.tvProductPrice.text = cartItem.price
            holder.binding.tvProductSize.text = cartItem.selectedSize
            holder.binding.tvProductColor.text = cartItem.selectedColor
            holder.binding.tvNumberOFItems.text = cartItem.count.toString()

            // Increase the quantity of the item in the cart.
            holder.binding.btnPlus.setOnClickListener {
                cartItem.count += 1
                notifyItemChanged(position)
                holder.binding.tvNumberOFItems.text = cartItem.count.toString()
                // Make the 'Checkout' button visible in the fragment.
                cartFragment.binding.btnDone.visibility = View.VISIBLE
                // Add the updated item to the list of items to update.
                viewModel.toUpdate.add(cartItem)
            }
            // Decrease the quantity of the item in the cart.
            holder.binding.btnMinus.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.count -= 1
                    notifyItemChanged(position)
                    holder.binding.tvNumberOFItems.text = cartItem.count.toString()
                    // Make the 'Checkout' button visible in the fragment.
                    cartFragment.binding.btnDone.visibility = View.VISIBLE
                    // Add the updated item to the list of items to update.
                    viewModel.toUpdate.add(cartItem)
                }
            }
        }
    }

    /**
     * Gets the cart item at the specified position.
     *
     * @param position The position of the item.
     * @return The cart item at the specified position.
     */
    fun getItem(position: Int): CartItems {
        return cartItems[position]
    }

    /**
     * Removes the cart item at the specified position.
     *
     * @param position The position of the item to remove.
     */
    fun removeItem(position: Int) {
        cartItems.removeAt(position)
        notifyItemRemoved(position)
    }
}