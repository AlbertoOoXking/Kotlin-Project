package com.albert.capstoneproject.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Favourites
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FavouritesListItemBinding
import com.albert.capstoneproject.viewModel.SharedViewModel


/**
 * Adapter for displaying favorite items in a RecyclerView.
 *
 * @param favItems List of favorite items to display in the RecyclerView.
 * @param viewModel Instance of SharedViewModel used for managing favorite items.
 */
class FavouritesAdapter(
    private var favItems: MutableList<Favourites>,
    private val viewModel: SharedViewModel
) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding the views of each favorite item.
     *
     * @param binding Binding object for the favorite item layout.
     */
    inner class ViewHolder(val binding: FavouritesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the layout for each favorite item and returns a ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The type of view to create.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FavouritesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Returns the number of favorite items in the list.
     *
     * @return The number of items in the favorites list.
     */
    override fun getItemCount(): Int {
        return favItems.size
    }

    /**
     * Binds the data for a specific favorite item to the ViewHolder.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = favItems[position]
        Log.e("FavouritesAdapter", "Binding item: ${fav.productName}")
        // Load product image into ImageView.
        holder.binding.ivFavouritesProduct.load(fav.image)
        // Set product name and price in the UI.
        holder.binding.tvProductName.text = fav.productName
        holder.binding.tvProductPrice.text = fav.productPrice

        // Handle click event for removing the item from favorites.
        holder.binding.btnFavourites.setOnClickListener {
            // Remove item from the favorites list in the ViewModel.
            viewModel.deleteFav(fav)
            // Remove the item from the adapter's list and update the RecyclerView.
            removeItem(position)
        }
    }

    /**
     * Gets the favorite item at the specified position.
     *
     * @param position The position of the item.
     * @return The favorite item at the specified position.
     */
    fun getItem(position: Int): Favourites {
        return favItems[position]
    }

    /**
     * Removes the favorite item at the specified position from the list and updates the RecyclerView.
     *
     * @param position The position of the item to remove.
     */
    fun removeItem(position: Int) {
        favItems.removeAt(position)
        notifyItemRemoved(position)
    }

}