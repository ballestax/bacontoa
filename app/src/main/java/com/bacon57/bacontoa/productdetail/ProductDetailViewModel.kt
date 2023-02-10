package com.bacon57.bacontoa.productdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.bacon57.bacontoa.model.*
import com.bacon57.bacontoa.repository.ProductRepository
import com.bacon57.bacontoa.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {


    private val _selectedIngredients = mutableListOf<Ingredient>()
    val selectedIngredients: List<Ingredient>
        get() = _selectedIngredients

    private val _selectedAdditional = mutableListOf<Additional>()
    val selectedAdditional: List<Additional>
        get() = _selectedAdditional

    private val _selectedPresentationId = mutableStateOf<Long>(0)
    val selectedPresentationId: Long
        get() = _selectedPresentationId.value

    fun toggleIngredientSelection(ingredient: Ingredient) {
        println("Ingredient:" + ingredient.name)
        if (_selectedIngredients.contains(ingredient)) {
            _selectedIngredients.remove(ingredient)
        } else {
            _selectedIngredients.add(ingredient)
        }
    }

    fun toggleAdditionalSelection(additional: Additional) {
        println("Additional:" + additional.name)
        if (_selectedAdditional.contains(additional)) {
            _selectedAdditional.remove(additional)
        } else {
            _selectedAdditional.add(additional)
        }
    }

    fun selectPresentation(presentationId: Long) {
        _selectedPresentationId.value = presentationId
    }

    suspend fun getProductInfo(productId: Long): Resource<Product> {
        return repository.getProductInfo(productId)
    }

    suspend fun getPresentationInfo(presentationId: Long): Resource<Presentation> {
        return repository.getPresentationInfo(presentationId)
    }



    fun getProductOrder(product: Product, price: BigDecimal): ProductOrder {
        return ProductOrder(
            productId = product.id,
            productName = product.name,
            price = price,
            presentationId = selectedPresentationId,
            exclusions = selectedIngredients,
            additions = selectedAdditional,
            quantity = 1
        )
    }

}
