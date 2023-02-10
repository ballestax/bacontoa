package com.bacon57.bacontoa.model

import java.io.Serializable

data class Additional(
    val id: Long,
    val name: String,
    val price: Double
): Serializable
