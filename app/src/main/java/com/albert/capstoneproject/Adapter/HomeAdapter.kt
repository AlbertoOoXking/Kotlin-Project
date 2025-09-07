package com.albert.capstoneproject.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.R
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.albert.capstoneproject.databinding.HomeListItemBinding


/**
 * Adapter for displaying a list of products in a RecyclerView on the home screen.
 *
 * @param product List of products to display in the RecyclerView.
 * @param viewModel Instance of SharedViewModel used for managing the currently selected product.
 */
class HomeAdapter(
    private var product: List<Product>,
    private var viewModel: SharedViewModel
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding the views of each product item.
     *
     * @param binding Binding object for the product item layout.
     */
    inner class ViewHolder(val binding: HomeListItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the layout for each product item and returns a ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The type of view to create.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Returns the number of products in the list.
     *
     * @return The number of items in the product list.
     */
    override fun getItemCount(): Int {
        return product.size
    }

    /**
     * Binds the data for a specific product item to the ViewHolder.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = product[position]

        // Set product details in the UI.
        holder.binding.productNameSingleProduct.text = product.title
        holder.binding.productImageSingleProduct.load(product.image)
        holder.binding.productPriceSingleProduct.text = product.price.toString()

        // Set the product rating in the UI.
        val rate = product.rating.rate.toFloat() ?: 0f
        holder.binding.productRatingSingleProduct.rating = rate

        // Set click listener for navigating to the product detail page.
        holder.binding.root.setOnClickListener {
            // Update the current product in the ViewModel.
            viewModel.currentProduct(product)
            // Navigate to the product detail fragment.
            holder.binding.root.findNavController().navigate(R.id.productDetailFragment)
        }
    }

    /**
     * Updates the product list and notifies the adapter of the change.
     *
     * @param productsList The new list of products to display.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(productsList: List<Product>) {
        product = productsList
        notifyDataSetChanged()
    }
}