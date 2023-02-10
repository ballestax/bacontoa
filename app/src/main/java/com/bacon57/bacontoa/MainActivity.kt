package com.bacon57.bacontoa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bacon57.bacontoa.model.ProductOrder
import com.bacon57.bacontoa.model.ProductOrderArgType
import com.bacon57.bacontoa.orderdetail.OrderDetailScreen
import com.bacon57.bacontoa.productdetail.ProductDetailScreen
import com.bacon57.bacontoa.productlist.ProductsListScreen
import com.bacon57.bacontoa.ui.theme.BaconTOATheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaconTOATheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "product_list_screen") {
                    composable("product_list_screen") {
                        ProductsListScreen(navController = navController)
                    }
                    composable(
                        "product_detail_screen/{id}",
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.LongType
                            }
                        )
                    ) {
                        val productId = remember {
                            it.arguments?.getLong("id")
                        }
                        ProductDetailScreen(
                            productId = productId!!,
                            navController = navController
                        )
                    }

                    composable(
                        "order_detail_screen/{product}",
                        arguments = listOf(
                            navArgument("product") {
                                type = ProductOrderArgType()
                            }
                        )
                    ) {
                        val productOrder = it.arguments?.getString("product")?.let { Gson().fromJson(it, ProductOrder::class.java) }
                        OrderDetailScreen(
                            productOrder = productOrder!!,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
