package com.bacon57.bacontoa.repository


import com.bacon57.bacontoa.data.BaconApi
import com.bacon57.bacontoa.model.*
import com.bacon57.bacontoa.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class OrderRepository @Inject constructor(

    private val api: BaconApi
) {
    suspend fun getOrderInfo(orderId: Long): Resource<Order> {
        val response = try {
            api.getOrder(orderId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }
}