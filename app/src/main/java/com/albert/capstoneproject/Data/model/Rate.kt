package com.albert.capstoneproject.Data.model


/**
 * Data class representing the rating information of a product.
 * This class holds the rating value and the number of reviews.
 *
 * @property rate The average rating of the product, typically a value between 0 and 5.
 * @property count The total number of reviews or ratings given to the product.
 */
data class Rate(
    var rate: Double, // The average rating of the product.
    var count: Int // The total number of reviews or ratings.
)
