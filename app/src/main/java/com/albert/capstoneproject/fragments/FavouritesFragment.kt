package com.albert.capstoneproject.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.albert.capstoneproject.Adapter.CartAdapter
import com.albert.capstoneproject.Adapter.FavouritesAdapter
import com.albert.capstoneproject.Adapter.HomeAdapter
import com.albert.capstoneproject.Adapter.OrderAdapter
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.R
import com.albert.capstoneproject.databinding.FragmentAddressInfoBinding
import com.albert.capstoneproject.databinding.FragmentFavouritesBinding
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding // Binding object for the fragment's layout
    private val viewModel: SharedViewModel by activityViewModels() // ViewModel for managing favorite items
    private lateinit var favouritesAdapter: FavouritesAdapter // Adapter for displaying favorites in RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.tv_titlo).text = "Favourites"

        // Observe the favouritesList LiveData from SharedViewModel
        viewModel.favouritesList.observe(viewLifecycleOwner) { fav ->
            Log.e("FavouritesFragment", "Favourites List: ${fav.size}")
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // Filter favorites to show only those associated with the current user
                val filteredList = fav.filter {
                    it.user == currentUser.email
                }
                val reversedList = filteredList.reversed()

                // Initialize the FavouritesAdapter with the filtered and reversed list
                favouritesAdapter = FavouritesAdapter(reversedList.toMutableList(), viewModel)
                binding.rvFavourites.adapter = favouritesAdapter

                // Set up swipe-to-delete functionality for the RecyclerView
                val swipeToDeleteCallback = SwipeToDeleteCallback()
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(binding.rvFavourites)
            }
        }
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
            val favAdapter = binding.rvFavourites.adapter as? FavouritesAdapter

            // Create an AlertDialog to confirm the removal of an item from favorites
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.apply {
                setTitle("Remove from Favorites")
                setMessage("Remove from Favourites?")
                setPositiveButton("Remove") { dialog, _ ->
                    val deletedItem = favAdapter?.getItem(position)
                    deletedItem?.let { viewModel.deleteFav(it) }
                    favAdapter?.removeItem(position)
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    // Notify adapter that item was not removed
                    this@FavouritesFragment.favouritesAdapter.notifyItemChanged(position)
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