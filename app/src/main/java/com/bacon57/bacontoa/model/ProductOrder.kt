package com.bacon57.bacontoa.model

import android.net.Uri
import com.bacon57.bacontoa.util.JsonNavType
import com.google.gson.Gson
import java.math.BigDecimal

data class ProductOrder(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val presentationId: Long,
//    val presentationName: String,
    val price: BigDecimal,
    val exclusions: List<Ingredient>,
    val additions: List<Additional>
) : java.io.Serializable {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class ProductOrderArgType : JsonNavType<ProductOrder>() {
    override fun fromJsonParse(value: String): ProductOrder =
        Gson().fromJson(value, ProductOrder::class.java)

    override fun ProductOrder.getJsonParse(): String = Gson().toJson(this)
}

