package com.bacon57.bacontoa.repository


import com.bacon57.bacontoa.data.BaconApi
import com.bacon57.bacontoa.model.CategoryList
import com.bacon57.bacontoa.model.Presentation
import com.bacon57.bacontoa.model.Product
import com.bacon57.bacontoa.model.ProductList
import com.bacon57.bacontoa.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ProductRepository @Inject constructor(

    private val api: BaconApi
) {
    suspend fun getProductList(): Resource<ProductList> {
        val response = try {
            api.getProductList()
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getProductInfo(productId: Long): Resource<Product> {
        val response = try {
            api.getProductInfo(productId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getPresentationInfo(presentationId: Long): Resource<Presentation> {
        val response = try {
            api.getPresentationInfo(presentationId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getCategoriesList(): Resource<CategoryList> {
        val response = try {
            api.getCategoryList()
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }
}