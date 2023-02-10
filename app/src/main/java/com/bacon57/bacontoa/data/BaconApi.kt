package com.bacon57.bacontoa.data

import com.bacon57.bacontoa.model.*
import retrofit2.http.GET
import retrofit2.http.Path

interface BaconApi {

    @GET("products")
    suspend fun getProductList(
    ): ProductList

    @GET("products/{id}")
    suspend fun getProductInfo(
        @Path("id") id:Long
    ): Product

    @GET("categories")
    suspend fun getCategoryList(
    ): CategoryList

    @GET("presentations/{id}")
    suspend fun getPresentationInfo(
        @Path("id") id:Long
    ): Presentation

    @GET("order/{id}")
    suspend fun getOrder(
        @Path("id") id:Long
    ): Order

}