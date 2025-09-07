package com.albert.capstoneproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.databinding.ProductsOrderListItemBinding

/**
 * Adapter for displaying a list of ordered products in a RecyclerView.
 *
 * @param model Instance of FireBaseViewModel used to access current user information.
 * @param product List of CartItems (ordered products) to display in the RecyclerView.
 */
class OrderedProductsAdapter(
    private val model: FireBaseViewModel,
    private var product: List<CartItems>,
) : RecyclerView.Adapter<OrderedProductsAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding the views of each ordered product item.
     *
     * @param binding Binding object for the ordered product item layout.
     */
    inner class ViewHolder(val binding: ProductsOrderListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the layout for each ordered product item and returns a ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The type of view to create.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): OrderedProductsAdapter.ViewHolder {
        val binding =
            ProductsOrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Binds the data for a specific ordered product item to the ViewHolder.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = product[position]

        // Check if the ordered product belongs to the current user.
        if (item.user == model.currentUser.value!!.email) {
            // Set ordered product details in the UI.
            holder.binding.ivCartProduct.load(item.image) // Load product image using Coil.
            holder.binding.tvProductName.text = item.productName // Set product name.
            holder.binding.tvPrice.text = item.price // Set product price.
            holder.binding.tvCount.text = item.count.toString() // Set product count.
        }
    }

    /**
     * Returns the number of ordered product items in the list.
     *
     * @return The number of items in the product list.
     */
    override fun getItemCount(): Int {
        return product.size
    }
}