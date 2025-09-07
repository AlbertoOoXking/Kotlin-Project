package com.albert.capstoneproject.Data.model


/**
 * Data class representing a product.
 * This class is used to hold details about a product in the application.
 *
 * @property id Unique identifier for the product.
 * @property title Name or title of the product.
 * @property price Price of the product in double format.
 * @property description A detailed description of the product.
 * @property category Category to which the product belongs (e.g., electronics, clothing).
 * @property image URL or path to the product's image.
 * @property rating Rating information of the product, encapsulated in a `Rate` object.
 */
data class Product(
    var id: Int = 0, // Unique identifier for the product.
    var title: String, // Name or title of the product.
    var price: Double, // Price of the product.
    var description: String, // Detailed description of the product.
    var category: String, // Category to which the product belongs.
    var image: String, // URL or path to the product's image.
    var rating: Rate // Rating information of the product.
)
