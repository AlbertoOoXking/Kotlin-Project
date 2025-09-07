package com.albert.capstoneproject.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.fragment.app.activityViewModels
import coil.load
import com.albert.capstoneproject.Adapter.OtherProductsAdapter
import com.albert.capstoneproject.R
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.albert.capstoneproject.databinding.FragmentProductDetailBinding


// Fragment to display details of a selected product
class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding // Binding object to access the views in the FragmentProductDetailBinding layout
    private val viewModel: SharedViewModel by activityViewModels() // SharedViewModel to manage product and favourite-related data

    // Inflate the layout for this fragment and return the root view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    // Called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the current product to display its details
        viewModel.currentProduct.observe(viewLifecycleOwner) { currentProduct ->
            // Observe the list of products to show other products in the same category
            viewModel.productList.observe(viewLifecycleOwner) { productList ->

                // Filter products by the same category as the current product
                val filteredProductList =
                    productList.filter { it.category == currentProduct.category }

                // Set up the RecyclerView with the filtered list of other products
                binding.rvOtherProducts!!.adapter =
                    OtherProductsAdapter(filteredProductList, viewModel)
            }
            // Load product details into the UI components
            binding.productImage.load(currentProduct.image)
            binding.productNameProductDetailsPage.text = currentProduct.title
            binding.productDesProductDetailsPage.text = currentProduct.description
            binding.RatingProductDetails.text = currentProduct.rating.rate.toString()
            val rate = currentProduct.rating.rate.toFloat()
            binding.productRatingSingleProduct.rating = rate
            binding.productPriceProductDetailsPage.text = currentProduct.price.toString()

            // Observe the list of favourite products to update the favourite icon
            viewModel.favouritesList.observe(viewLifecycleOwner) { favList ->
                val isFavourite = favList.any { it.productName == currentProduct.title }
                updateHeartIcon(isFavourite)  // Update the heart icon

                // Handle click event to add/remove product from favourites
                binding.productAddToFavSingleProduct!!.setOnClickListener {
                    if (isFavourite) {
                        // Remove from favourites
                        val favProduct = favList.find { it.productName == currentProduct.title }
                        favProduct?.let {
                            viewModel.deleteFav(it)
                            Toast.makeText(
                                requireContext(), "Removed from Favourites", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Add to favourites
                        viewModel.insertFav(currentProduct)
                        Toast.makeText(requireContext(), "Added to Favourites", Toast.LENGTH_SHORT)
                            .show()
                    }
                    updateHeartIcon(!isFavourite)  // Update the heart icon
                }
            }
            // Handle click event to show the product options bottom sheet
            binding.ivAddToCart.setOnClickListener {
                setUpSheet()
            }
            // Animate the product description text view for expanding/collapsing
            animate()
        }
    }

    // Update the heart icon based on whether the product is in favourites
    private fun updateHeartIcon(isFavourite: Boolean) {
        if (isFavourite) {
            binding.ivProductAddToFav!!.setImageResource(R.drawable.baseline_favorite_border_24)
        } else {
            binding.ivProductAddToFav!!.setImageResource(R.drawable.baseline_heart_broken_24)
        }
    }

    // Show the bottom sheet for adding the product to the cart
    private fun setUpSheet() {
        binding.apply {
            val sheet = ProductBottomSheetFragment()
            sheet.show(parentFragmentManager, ProductBottomSheetFragment.TAG)
        }
    }

    // Animate the expansion and collapse of the product description
    private fun animate() {
        var isOpen = false
        binding.ivOpenDescription?.setOnClickListener {
            val textView = binding.productDesProductDetailsPage
            val isExpanding = !isOpen

            // Define the number of lines for expanding and collapsing
            val startMaxLines = if (isExpanding) 2 else 100
            val endMaxLines = if (isExpanding) 100 else 2

            // Measure the height of the text view for the given number of lines
            val startHeight = measureHeightForMaxLines(textView, startMaxLines)
            val endHeight = measureHeightForMaxLines(textView, endMaxLines)

            // Create an animator to interpolate between the start and end heights
            val animator = ValueAnimator.ofInt(startHeight, endHeight)
            animator.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                val layoutParams = textView.layoutParams
                layoutParams.height = animatedValue
                textView.layoutParams = layoutParams
            }
            // Update the maxLines property after the animation ends
            animator.addListener(onEnd = {
                textView.maxLines = endMaxLines
                textView.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            })

            animator.duration = 500 // Duration of the animation
            animator.start()

            isOpen = !isOpen
        }
    }

    // Measure the height of the TextView for a specific number of lines
    private fun measureHeightForMaxLines(textView: TextView, maxLines: Int): Int {
        val widthMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(textView.width, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        textView.maxLines = maxLines
        textView.measure(widthMeasureSpec, heightMeasureSpec)
        return textView.measuredHeight
    }
}