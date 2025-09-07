package com.albert.capstoneproject.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.albert.capstoneproject.Data.OrderRepository
import com.albert.capstoneproject.Data.CartRepository
import com.albert.capstoneproject.Data.FavouritesRepository
import com.albert.capstoneproject.Data.PersonalInfoRepository
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Favourites
import com.albert.capstoneproject.Data.model.Order
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.Data.remote.ApiService
import com.albert.capstoneproject.local.PersonalInfoDatabase.getAllInfo
import com.albert.capstoneproject.local.orderDatabase.getData
import com.albert.capstoneproject.local.cartDatabase.getDatabase
import com.albert.capstoneproject.local.favouritesDatabase.getAllData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


/**
 * ViewModel for managing and interacting with data related to user personal information, cart items, orders, and favourites.
 */
open class SharedViewModel(application: Application) : AndroidViewModel(application) {

    // List of items to update in the cart
    val toUpdate: MutableList<CartItems> = mutableListOf()

    // Database instances for personal info, favourites, cart, and orders
    private val personal = getAllInfo(application)
    private val favData = getAllData(application)
    private val data = getData(application)
    private val database = getDatabase(application)

    // Repositories for interacting with different data sources
    private val personalInfoRepository = PersonalInfoRepository(personal)
    private val cartRepository = CartRepository(ApiService.ShopApi, database)
    private val orderRepository = OrderRepository(data)
    private val favRepository = FavouritesRepository(favData)

    // LiveData for observing personal information, favourites, cart items, orders, and products
    val personalInfo = personalInfoRepository.personalInformation
    val favouritesList = favRepository.favList
    val cartList = cartRepository.cartItems
    val orderList = orderRepository.orderList
    val productList = cartRepository.allProducts


    //       Personal DATABASE      //

    // Initialization block to fetch personal info on ViewModel creation
    init {
        getInfo()
    }

    // Fetch personal information from the repository
    fun getInfo() {
        viewModelScope.launch {
            try {
                personalInfoRepository.getInfo()
            } catch (e: Exception) {
                Log.e("TAG", "Error writing into database: $e")
            }
        }
    }

    // Insert new personal information into the repository
    fun insertInfo(info: PersonalInfo) {
        viewModelScope.launch {
            personalInfoRepository.insertInfo(info)
        }
    }

    // Update existing personal information in the repository
    fun updateInfo(info: PersonalInfo) {
        viewModelScope.launch {
            Log.e("ViewModel", "updated user with id: ${info.infoId}")
            personalInfoRepository.updateInfo(info)
        }
    }

    // Delete personal information from the repository
    fun deleteInfo(info: PersonalInfo) {
        viewModelScope.launch {
            Log.e("ViewModel", "Deleted user with id: ${info.infoId}")
            personalInfoRepository.deleteInfo(info)
        }
    }


    //       Cart DATABASE      //

    // Insert an item into the cart, updating quantity if the item already exists
    fun insertCart(item: CartItems) {
        viewModelScope.launch {
            val existingCartItems = cartList.value ?: emptyList()
            val existingItem = existingCartItems.find {
                it.productName == item.productName && it.selectedColor == item.selectedColor && it.selectedSize == item.selectedSize && it.user == item.user
            }
            if (existingItem != null) {
                val updatedItem = existingItem.copy(count = existingItem.count + item.count)
                update(updatedItem)
            } else {
                cartRepository.insert(item)
            }
        }
    }

    // Update an item in the cart
    fun update(item: CartItems) {
        viewModelScope.launch {
            Log.e("ViewModel", "updated user with id: ${item.id}")

            cartRepository.update(item)
        }
    }

    // Delete an item from the cart
    fun delete(item: CartItems) {
        viewModelScope.launch {
            Log.e("ViewModel", "Deleted user with id: ${item.id}")
            cartRepository.delete(item)
        }
    }

    //       Order DATABASE      //

    // Insert a new order into the order repository
    fun insertOrder(items: List<CartItems>, totalPrice: Double) {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val order = Order(
                    user = currentUser.email!!, items = items, totalPrice = totalPrice
                )
                orderRepository.insertOrder(order)
            }
        }
    }

    // Update an existing order
    fun updateOrder(orderItem: Order) {
        viewModelScope.launch {
            Log.e("ViewModel", "updated user with id: ${orderItem.orderId}")

            orderRepository.update(orderItem)
        }
    }

    // Delete an order from the repository
    fun deleteOrder(orderItem: Order) {
        viewModelScope.launch {
            Log.e("ViewModel", "Deleted user with id: ${orderItem.orderId}")
            orderRepository.delete(orderItem)
        }
    }

    //       Fav DATABASE      //

    // Insert a product into the user's favourites list
    fun insertFav(product: Product) {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val fav = Favourites(
                    user = currentUser.email ?: "Error",
                    productName = product.title,
                    productPrice = product.price.toString(),
                    image = product.image,
                    isFav = true
                )
                favRepository.insertFavourites(fav)
                Log.d("ViewModel", "Inserted new favourite item: ${product.title}")

            } else {
                Log.e("ViewModel", "User not authenticated. Cannot insert favourite.")
            }
        }
    }

    // Update an existing favourite item
    fun updateFav(favItem: Favourites) {
        viewModelScope.launch {
            Log.e("ViewModel", "updated user with id: ${favItem.favouriteId}")
            favRepository.update(favItem)
        }
    }

    // Delete a favourite item from the repository
    fun deleteFav(favItem: Favourites) {
        viewModelScope.launch {
            Log.e("ViewModel", "Deleted user with id: ${favItem.favouriteId}")
            favRepository.delete(favItem)
            favItem.isFav = false
        }
    }

    // LiveData for observing the current product, total price, and item count
    private var _product = MutableLiveData<List<Product>>()
    val product: LiveData<List<Product>>
        get() = _product

    ////////////////////////////////////////////////////////
    private var _currentProduct = MutableLiveData<Product>()
    val currentProduct: LiveData<Product>
        get() = _currentProduct

    ////////////////////////////////////////////////////////
    private var _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice

    ////////////////////////////////////////////////////////
    private var _itemCount = MutableLiveData<Int>()
    var itemCount: LiveData<Int> = _itemCount


    fun currentProduct(selectedProduct: Product) { // Set the current product
        _currentProduct.value = selectedProduct
    }

    fun updateTotalPrice(newPrice: Double) { // Update the total price
        _totalPrice.value = newPrice
    }

    fun loadProducts() { // Load products from the cart repository
        viewModelScope.launch {
            cartRepository.getProducts()
        }
    }


    fun plusArticle() { // Increment the item count
        val updatedCount = (_itemCount.value ?: 1) + 1
        _itemCount.value = updatedCount
        cartRepository.updateItemCount(updatedCount)
    }

    fun minusArticle() { // Decrement the item count
        val currentCount = _itemCount.value ?: 1
        if (currentCount > 1) {
            val updateCount = currentCount - 1
            _itemCount.value = updateCount
            cartRepository.updateItemCount(updateCount)
        }
    }

    fun resetArticle() { // Reset the item count to 1
        _itemCount.value = 1
        cartRepository.updateItemCount(1)
    }
}