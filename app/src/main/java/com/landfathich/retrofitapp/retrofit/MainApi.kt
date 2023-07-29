package com.landfathich.retrofitapp.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {
    @GET("auth/product/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>

    @Headers("Content-Type: application/gson")
    @GET("auth/products")
    suspend fun getAllProductsAuth(@Header("Authorization") token: String): Products

    @Headers("Content-Type: application/gson")
    @GET("auth/products/search")
    suspend fun getProductsByNameAuth(
        @Header("Authorization") token: String,
        @Query("q") name: String,
    ): Products
}