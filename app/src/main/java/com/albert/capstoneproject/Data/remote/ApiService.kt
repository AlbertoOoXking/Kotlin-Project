package com.albert.capstoneproject.Data.remote

import com.albert.capstoneproject.Data.model.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Base URL for the API endpoint
private const val BASE_URL = "https://fakestoreapi.com/"

// Logger for HTTP requests and responses
private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

// OkHttpClient instance with the logging interceptor
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

// Moshi instance for JSON serialization/deserialization
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit instance configured with Moshi and OkHttp
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

// Define the API service interface for network requests
interface ApiService {

    // Function to fetch a list of products from the API
    @GET("products")
    suspend fun loadProducts(): List<Product>

    // Object to provide the ApiService instance
    object ShopApi {
        val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
    }
}