package com.albert.capstoneproject.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.albert.capstoneproject.databinding.OtherProductsListItemBinding


/**
 * Adapter for displaying a list of other products in a RecyclerView.
 *
 * @param product List of Product items to be displayed.
 * @param viewModel Instance of SharedViewModel used to handle product selection.
 */
class OtherProductsAdapter(
    private var product: List<Product>,
    private var viewModel: SharedViewModel
) : RecyclerView.Adapter<OtherProductsAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding the views of each product item.
     *
     * @param binding Binding object for the product item layout.
     */
    inner class ViewHolder(val binding: OtherProductsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the layout for each product item and returns a ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The type of view to create.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OtherProductsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Returns the number of product items in the list.
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
        holder.binding.productNameSingleProduct.text = product.title // Set product name.
        holder.binding.productImageSingleProduct.load(product.image) // Load product image using Coil.
        holder.binding.productPriceSingleProduct.text = product.price.toString() // Set product price.

        // Set product rating in the UI. Ensure it defaults to 0 if the rate is null.
        val rate = product.rating.rate.toFloat() ?: 0f
        holder.binding.productRatingSingleProduct.rating = rate

        // Set a click listener to update the current product in the ViewModel.
        holder.binding.root.setOnClickListener {
            viewModel.currentProduct(product) // Pass the selected product to the ViewModel.
        }
    }
}