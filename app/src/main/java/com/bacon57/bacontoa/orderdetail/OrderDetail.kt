package com.bacon57.bacontoa.orderdetail

import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bacon57.bacontoa.model.ProductOrder
import com.bacon57.bacontoa.productdetail.ProductDetailViewModel
import java.util.*

@Composable
fun OrderDetailScreen(
    productOrder: ProductOrder,
    navController: NavController
) {

//    val productInfo = produceState<Resource<Product>>(initialValue = Resource.Loading()) {
//        value = viewModel.getProductInfo(productId)
//    }.value

    Column(modifier = Modifier.fillMaxSize()) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Orden:")
            Text(
                text = "# 00001",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Estado",
                fontSize = 18.sp,
                color = Color.Green,
                modifier = Modifier.padding(8.dp)
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Mesero:")
            Text(
                text = "Mesero 1",
                fontSize = 18.sp
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Mesa:")
            Text(
                text = "Mesa 1",
                fontSize = 18.sp
            )
        }

        LazyColumn {
            item {
                ProductOrderItem(productOrder = productOrder)
            }
        }
    }
}

@Composable
fun ProductOrderItem(
    productOrder: ProductOrder
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .weight(.25f)
                .align(CenterVertically), horizontalArrangement = Arrangement.Center
        ) {

            Row {
                Button(modifier = Modifier, onClick = {
                }) {
                    Icon(
                        painterResource(id = com.bacon57.bacontoa.R.drawable.ic_baseline_arrow_drop_up_24),
                        contentDescription = null,
                    )
                }
                Text(
                    text = "${productOrder.quantity}",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Button(modifier = Modifier, onClick = {
                }) {
                    Icon(
                        painterResource(id = com.bacon57.bacontoa.R.drawable.ic_baseline_arrow_drop_down_24),
                        contentDescription = null,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(.5f)
                .padding(horizontal = 8.dp)

        ) {
            Text(
                text = productOrder.productName.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            if (productOrder.exclusions.isNotEmpty())
                Text(text = "Sin: ${productOrder.exclusions.joinToString { it.name.capitalize(Locale.ROOT) }}")
            Column() {
                productOrder.additions.forEach {
                    Text(
                        text = " + ${it.name.capitalize(Locale.ROOT)} (${
                            String.format(
                                "%,.0f",
                                it.price
                            )
                        })"
                    )
                }
            }
        }

        Text(
            text = String.format("$%,.0f", productOrder.price.toDouble()),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(.2f)
                .align(CenterVertically)
                .padding(8.dp)
        )
    }
}


