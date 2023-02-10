package com.bacon57.bacontoa.model

import java.io.Serializable

data class Presentation(
    val id: Long,
    val name: String,
    val price: Double,
    val _enabled: Boolean,
    val _default: Boolean
): Serializable
