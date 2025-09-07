package com.albert.capstoneproject.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.albert.capstoneproject.Adapter.HomeAdapter
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.R
import com.albert.capstoneproject.viewModel.SharedViewModel
import com.albert.capstoneproject.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: SharedViewModel by activityViewModels()

    private var selectedCategory: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.loadProducts() // Load the products from the ViewModel
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.tv_titlo).text = "Store"


        viewModel.productList.observe(viewLifecycleOwner) { product ->
            setupCategoryFilter(product) // Set up category filter based on the products
        }
        binding.ivFilter.setOnClickListener {
            showFilterDialog() // Show dialog to filter products by price
        }
        binding.ivSearch.setOnClickListener {
            if (binding.etSearch.visibility == View.VISIBLE) {
                binding.etSearch.visibility = View.GONE
            } else {
                binding.etSearch.visibility = View.VISIBLE
                binding.etSearch.requestFocus()
                finalSearch() // Set up search functionality
            }
        }
    }

    private fun finalSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search(s.toString())
            }
        })
    }

    private fun search(text: String) {
        val products = viewModel.productList.value ?: return
        val filteredByText = products.filter {
            it.title.contains(text, ignoreCase = true)
        }
        val filteredByCategory = if (selectedCategory == null) {
            filteredByText
        } else {
            filteredByText.filter { it.category == selectedCategory }
        }
        (binding.rvProducts.adapter as HomeAdapter).updateDataset(filteredByCategory)
    }

    private fun setupCategoryFilter(products: List<Product>) {
        binding.rvProducts.adapter = HomeAdapter(products, viewModel)

        binding.cvAll.setOnClickListener {
            selectedCategory = null
            search(binding.etSearch.text.toString()) // Update search results
            binding.rvProducts.adapter = HomeAdapter(products, viewModel)
        }

        binding.cvElectronics.setOnClickListener {
            selectedCategory = "electronics"
            search(binding.etSearch.text.toString()) // Update search results
            filterByCategoryAndPrice(products) // Apply category and price filter
        }

        binding.cvJewelery.setOnClickListener {
            selectedCategory = "jewelery"
            search(binding.etSearch.text.toString()) // Update search results
            filterByCategoryAndPrice(products) // Apply category and price filter
        }

        binding.cvMenClothing.setOnClickListener {
            selectedCategory = "men's clothing"
            search(binding.etSearch.text.toString()) // Update search results
            filterByCategoryAndPrice(products) // Apply category and price filter
        }

        binding.cvWomenClothing.setOnClickListener {
            selectedCategory = "women's clothing"
            search(binding.etSearch.text.toString()) // Update search results
            filterByCategoryAndPrice(products) // Apply category and price filter
        }
    }

    private fun filterByCategoryAndPrice(
        products: List<Product>,
    ) {
        val filteredList = products.filter { product ->
            (selectedCategory == null || product.category == selectedCategory)
        }
        if (filteredList.isNotEmpty()) {
            (binding.rvProducts.adapter as HomeAdapter).updateDataset(filteredList)
        } else {
            Toast.makeText(
                requireContext(), "No products found in this price range", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showFilterDialog() {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_filter_price, null)
        val minPriceEditText = dialogView.findViewById<EditText>(R.id.et_min_price)
        val maxPriceEditText = dialogView.findViewById<EditText>(R.id.et_max_price)

        AlertDialog.Builder(requireContext()).setTitle("Filter by Price").setView(dialogView)
            .setPositiveButton("Apply") { dialog, _ ->
                val minPriceText = minPriceEditText.text.toString()
                val maxPriceText = maxPriceEditText.text.toString()

                val minPrice = if (minPriceText.isNotEmpty()) minPriceText.toDouble() else 0.0
                val maxPrice =
                    if (maxPriceText.isNotEmpty()) maxPriceText.toDouble() else Double.MAX_VALUE

                filterProductsByPrice(minPrice, maxPrice)
                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun filterProductsByPrice(minPrice: Double, maxPrice: Double) {
        val filteredList = viewModel.productList.value?.filter { product ->
            product.price in minPrice..maxPrice
        }

        if (filteredList != null) {
            filterByCategoryAndPrice(filteredList) // Apply category filter after price filter
        } else {
            Toast.makeText(
                requireContext(), "No products found in this price range", Toast.LENGTH_SHORT
            ).show()
        }
    }
}