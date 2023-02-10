package com.bacon57.bacontoa.model

import java.io.Serializable

data class Product(
    val id: Long,
    val name: String,
    val code: String,
    val category: String,
    val image: String,
    val price: Double,
    val variable: Boolean,
    val presentations: List<Presentation>,
    val ingredients: List<Ingredient>,
    val additionals: List<Additional>
) : Serializable
