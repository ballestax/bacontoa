package com.bacon57.bacontoa.productlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacon57.bacontoa.model.Category
import com.bacon57.bacontoa.model.Product
import com.bacon57.bacontoa.repository.ProductRepository
import com.bacon57.bacontoa.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    var productList = mutableStateOf<List<Product>>(listOf())
    var categoriesList = mutableStateOf<List<Category>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var filter = mutableStateOf("")

    private var cachedProductList = listOf<Product>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        println("Init viewmodel")
        loadCategories()
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            println("Loading products...")
            isLoading.value = true
            when (val result = repository.getProductList()) {
                is Resource.Success -> {
                    loadError.value = ""
                    productList.value = result.data!!
                    isLoading.value = false
                    println("Loaded products")
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                    println("Error loading products")
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            isLoading.value = true
            println("Loading categories...")
            val result = repository.getCategoriesList()
            when (result) {
                is Resource.Success -> {
                    loadError.value = ""
                    categoriesList.value = result.data!!
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

}